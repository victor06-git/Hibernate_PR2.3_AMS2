package com.project.sqliteutils;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Utilitats per a la gestió de connexions i metadades de SQLite.
 * Refactoritzat per millorar la seguretat de tipus i la gestió d'excepcions.
 */
public class UtilsSQLite {

    // Definim el prefix JDBC com a constant
    private static final String JDBC_PREFIX = "jdbc:sqlite:";

    /**
     * Estableix connexió amb la base de dades.
     * * @param filePath Ruta absoluta al fitxer de la base de dades.
     * @return L'objecte Connection obert.
     * @throws SQLException Si hi ha un error de connexió.
     */
    public static Connection connect(String filePath) throws SQLException {
        String url = JDBC_PREFIX + filePath;
        // DriverManager.getConnection pot llançar SQLException, la passem amunt
        Connection conn = DriverManager.getConnection(url);
        
        if (conn != null) {
            DatabaseMetaData meta = conn.getMetaData();
            System.out.println("✅ Connectat a SQLite. Driver: " + meta.getDriverName());
        }
        return conn;
    }

    /**
     * Tanca la connexió de manera segura.
     * * @param conn La connexió a tancar.
     */
    public static void disconnect(Connection conn) {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
                System.out.println("✅ BBDD SQLite desconnectada correctament.");
            }
        } catch (SQLException ex) {
            System.err.println("❌ Error tancant la connexió: " + ex.getMessage());
        }
    }

    /**
     * Obté una llista amb els noms de totes les taules d'usuari de la base de dades.
     * * @param conn La connexió activa.
     * @return Llista de strings amb els noms de les taules.
     * @throws SQLException Si falla la lectura de metadades.
     */
    public static List<String> listTables(Connection conn) throws SQLException {
        List<String> list = new ArrayList<>();
        DatabaseMetaData meta = conn.getMetaData();
        
        // El quart paràmetre "TABLE" filtra per obtenir només taules (ignora vistes, etc.)
        try (ResultSet rs = meta.getTables(null, null, null, new String[]{"TABLE"})) {
            while (rs.next()) {
                list.add(rs.getString("TABLE_NAME"));
            }
        }
        return list;
    }
}