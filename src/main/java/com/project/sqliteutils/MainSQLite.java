package com.project.sqliteutils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class MainSQLite {

    public static void main(String[] args) {
        // Ús de Path per a una millor compatibilitat de rutes (Windows/Linux/Mac)
        Path dbPath = Paths.get(System.getProperty("user.dir"), "data", "database-pr23.db");

        // Verificació prèvia
        if (!Files.exists(dbPath)) {
            System.err.println("❌ Error: No s'ha trobat el fitxer de base de dades a: " + dbPath);
            // Opcional: Podríem deixar que el driver la creï buida, però en aquest cas volem llegir dades.
        }

        System.out.println("📂 Intentant connectar a: " + dbPath);

        // Bloc try-with-resources: Tanca la connexió automàticament al final
        try (Connection conn = UtilsSQLite.connect(dbPath.toString())) {

            // 1. Obtenir llista de taules
            List<String> taules = UtilsSQLite.listTables(conn);
            System.out.println("📋 Taules trobades: " + taules);

            // 2. Iterar i mostrar contingut
            for (String nomTaula : taules) {
                // Filtrem taules internes de SQLite si calgués (sqlite_sequence, etc.)
                if (!nomTaula.startsWith("sqlite_")) {
                    printTableContent(conn, nomTaula);
                }
            }

        } catch (SQLException e) {
            System.err.println("❌ Error de SQL crític: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Mètode auxiliar per consultar i imprimir el contingut d'una taula.
     * Encapsula la creació del Statement i ResultSet per assegurar-ne el tancament.
     */
    private static void printTableContent(Connection conn, String tableName) {
        String sql = "SELECT * FROM " + tableName;
        System.out.println("\n------------------------------------------------");
        System.out.println("🔍 CONTINGUT DE LA TAULA: " + tableName);
        System.out.println("------------------------------------------------");

        // Try-with-resources niat per tancar Statement i ResultSet
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            ResultSetMetaData rsmd = rs.getMetaData();
            int numColumns = rsmd.getColumnCount();

            // 1. Imprimir Capçaleres i Tipus
            System.out.println("--> COLUMNES:");
            for (int i = 1; i <= numColumns; i++) {
                System.out.printf("    %-20s | %s (%s)%n", 
                    rsmd.getColumnName(i), 
                    rsmd.getColumnTypeName(i), 
                    rsmd.getColumnClassName(i)); // Més informatiu que el codi numèric
            }

            // 2. Imprimir Dades
            System.out.println("\n--> DADES:");
            int rowCount = 0;
            while (rs.next()) {
                StringBuilder rowText = new StringBuilder("    Row " + (++rowCount) + ": ");
                
                for (int i = 1; i <= numColumns; i++) {
                    if (i > 1) rowText.append(", ");
                    
                    String colName = rsmd.getColumnName(i);
                    Object value = rs.getObject(i); // getObject gestiona automàticament els tipus
                    
                    // Gestionar valors NULL per evitar imprimir "null" lleig o errors
                    String valStr = (value != null) ? value.toString() : "[NULL]";
                    
                    rowText.append(colName).append("=").append(valStr);
                }
                System.out.println(rowText);
            }

            if (rowCount == 0) {
                System.out.println("    (La taula està buida)");
            }

        } catch (SQLException e) {
            System.err.println("⚠️ Error llegint la taula " + tableName + ": " + e.getMessage());
        }
    }
}