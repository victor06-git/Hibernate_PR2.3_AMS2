package com.project;

import java.io.File;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.project.dao.Manager;
import com.project.domain.*;

/**
 * Classe principal per provar el sistema de gestió de biblioteca.
 * 
 * INSTRUCCIONS PER L'ALUMNE:
 * 1. Primer implementa les anotacions JPA a les entitats (paquet domain)
 * 2. Després implementa els mètodes del Manager (paquet dao)
 * 3. Descomenta cada FASE progressivament per provar el codi implementat
 * 4. No descomentis una fase fins que l'anterior funcioni correctament
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("Iniciant PR2.3 Gestió de Biblioteca...");

        // ---------------------------------------------------------
        // FASE 0: CONFIGURACIÓ INICIAL
        // Aquesta fase ja està implementada i hauria de funcionar
        // si les entitats tenen les anotacions @Entity bàsiques.
        // ---------------------------------------------------------
        
        // Creem el directori data/ si no existeix (on es guardarà la BD SQLite)
        String basePath = System.getProperty("user.dir") + "/data/";
        File dir = new File(basePath);
        if (!dir.exists()) dir.mkdirs();

        // Inicialitzem Hibernate - Si falla aquí, revisa les anotacions @Entity a les entitats
        Manager.createSessionFactory();

        // Variables per emmagatzemar els objectes creats i reutilitzar-los entre fases
        Autor a1 = null, a2 = null;
        Llibre l1 = null, l2 = null;
        Biblioteca biblio = null;
        Exemplar ex1 = null, ex2 = null;
        Persona p1 = null;
        Prestec prestec1 = null;

        // ---------------------------------------------------------
        // FASE 1: CREACIÓ D'ENTITATS SIMPLES
        // Objectiu: Implementar addAutor() i addLlibre() al Manager
        // Prerequisit: Les entitats Autor i Llibre han de tenir @Entity, @Id, @GeneratedValue
        // ---------------------------------------------------------
        
        /*
        System.out.println("\n=== FASE 1: Creació d'Autors i Llibres ===");
        
        // Creem dos autors
        a1 = Manager.addAutor("George Orwell");
        a2 = Manager.addAutor("J.K. Rowling");
        
        // Creem dos llibres (encara sense autors assignats)
        l1 = Manager.addLlibre("ISBN-111", "1984", "Editorial A", 1949);
        l2 = Manager.addLlibre("ISBN-222", "Harry Potter 1", "Editorial B", 1997);
        
        // Comprovem que s'han creat correctament (no són null i tenen ID assignat)
        System.out.println("Autors creats: " + (a1 != null && a2 != null ? "OK" : "ERROR"));
        System.out.println("Llibres creats: " + (l1 != null && l2 != null ? "OK" : "ERROR"));
        */

        // ---------------------------------------------------------
        // FASE 2: RELACIONS MANY-TO-MANY (Autors <-> Llibres)
        // Objectiu: Implementar updateAutor() al Manager
        // Prerequisit: Relacions @ManyToMany configurades a Autor i Llibre
        // COMPTE: Recorda que Llibre és el costat propietari de la relació!
        // ---------------------------------------------------------
        
        /*
        System.out.println("\n=== FASE 2: Vinculació Autors-Llibres ===");
        
        // Assignem el llibre "1984" a l'autor George Orwell
        if (a1 != null && l1 != null) {
            Set<Llibre> llibresOrwell = new HashSet<>(); 
            llibresOrwell.add(l1);
            Manager.updateAutor(a1.getAutorId(), a1.getNom(), llibresOrwell);
            System.out.println("Autor '" + a1.getNom() + "' vinculat al llibre '" + l1.getTitol() + "'");
        }
        
        // Assignem el llibre "Harry Potter 1" a l'autora J.K. Rowling
        if (a2 != null && l2 != null) {
            Set<Llibre> llibresRowling = new HashSet<>(); 
            llibresRowling.add(l2);
            Manager.updateAutor(a2.getAutorId(), a2.getNom(), llibresRowling);
            System.out.println("Autor '" + a2.getNom() + "' vinculat al llibre '" + l2.getTitol() + "'");
        }
        */

        // ---------------------------------------------------------
        // FASE 3: INFRAESTRUCTURA (Biblioteca, Exemplar, Persona)
        // Objectiu: Implementar addBiblioteca(), addExemplar(), addPersona()
        // Prerequisit: Relacions @ManyToOne i @OneToMany configurades
        // ---------------------------------------------------------
        
        /*
        System.out.println("\n=== FASE 3: Infraestructura i Usuaris ===");
        
        // Creem una biblioteca
        biblio = Manager.addBiblioteca("Biblioteca Central", "Barcelona", "C/ Gran 1", "930000000", "info@bib.cat");
        System.out.println("Biblioteca creada: " + (biblio != null ? "OK" : "ERROR"));
        
        // Creem exemplars físics dels llibres a la biblioteca
        // Un Exemplar és una còpia física concreta d'un Llibre ubicada a una Biblioteca
        if (l1 != null && biblio != null) {
            ex1 = Manager.addExemplar("COD-1984-A", l1, biblio);
            System.out.println("Exemplar de '1984' creat: " + (ex1 != null ? "OK" : "ERROR"));
        }
        if (l2 != null && biblio != null) {
            ex2 = Manager.addExemplar("COD-HP-A", l2, biblio);
            System.out.println("Exemplar de 'Harry Potter' creat: " + (ex2 != null ? "OK" : "ERROR"));
        }
        
        // Creem un usuari de la biblioteca
        p1 = Manager.addPersona("12345678Z", "Maria User", "666777888", "maria@test.com");
        System.out.println("Persona creada: " + (p1 != null ? "OK" : "ERROR"));
        
        // Mostrem tots els exemplars creats
        System.out.println("\n>> Llistat d'Exemplars a la base de dades:");
        System.out.println(Manager.collectionToString(Exemplar.class, Manager.listCollection(Exemplar.class)));
        */

        // ---------------------------------------------------------
        // FASE 4: LÒGICA DE PRÉSTECS
        // Objectiu: Implementar addPrestec() i registrarRetornPrestec()
        // Prerequisit: Entitat Prestec amb relacions @ManyToOne configurades
        // IMPORTANT: Inclou la lògica de negoci (comprovar disponibilitat)
        // ---------------------------------------------------------
        
        /*
        System.out.println("\n=== FASE 4: Préstecs i Retorns ===");
        LocalDate avui = LocalDate.now();

        // 4.1. Préstec correcte - L'exemplar està disponible
        System.out.println("\n-> Intentant prestar exemplar '1984' a Maria...");
        if (ex1 != null && p1 != null) {
            prestec1 = Manager.addPrestec(ex1, p1, avui, avui.plusDays(15));
            System.out.println("Préstec creat: " + (prestec1 != null ? "OK" : "ERROR - Revisa la implementació"));
        }

        // 4.2. Préstec incorrecte - Intentem prestar el mateix exemplar (ja prestat)
        // Això hauria de mostrar un missatge d'error i retornar null
        System.out.println("\n-> Intentant prestar EL MATEIX exemplar (ha de fallar)...");
        Prestec prestecFallat = Manager.addPrestec(ex1, p1, avui, avui.plusDays(15));
        System.out.println("Préstec rebutjat correctament: " + (prestecFallat == null ? "OK" : "ERROR - Hauria de ser null!"));

        // 4.3. Retorn del préstec
        System.out.println("\n-> Retornant el préstec...");
        if (prestec1 != null) {
            Manager.registrarRetornPrestec(prestec1.getPrestecId(), avui.plusDays(5));
            System.out.println("Retorn registrat.");
        }
        
        // Mostrem l'estat final dels préstecs
        System.out.println("\n>> Estat final dels Préstecs:");
        System.out.println(Manager.collectionToString(Prestec.class, Manager.listCollection(Prestec.class)));
        */

        // ---------------------------------------------------------
        // FASE 5: CONSULTES HQL
        // Objectiu: Implementar les 3 consultes al Manager
        // Prerequisit: Totes les fases anteriors completades
        // ---------------------------------------------------------
        
        /*
        System.out.println("\n=== FASE 5: Consultes HQL ===");
        
        // Consulta A: Llibres amb els seus autors (usa JOIN FETCH i DISTINCT)
        System.out.println("\n--- A) Llibres amb Autors ---");
        List<Llibre> llibresAutors = Manager.findLlibresAmbAutors();
        if (llibresAutors != null && !llibresAutors.isEmpty()) {
            for (Llibre l : llibresAutors) {
                System.out.println("   - " + l.getTitol() + " (" + l.getAutors().size() + " autor/s)");
            }
        } else {
            System.out.println("   [Cap resultat o mètode no implementat]");
        }

        // Consulta B: Llibres actualment en préstec (préstecs actius)
        // NOTA: Per veure resultats aquí, comenta la part del 'Retorn' a la Fase 4
        System.out.println("\n--- B) Llibres actualment en préstec ---");
        List<Object[]> enPrestec = Manager.findLlibresEnPrestec();
        System.out.println(Manager.formatMultipleResult(enPrestec));

        // Consulta C: Quins llibres estan a quines biblioteques
        System.out.println("\n--- C) Llibres i les seves Biblioteques ---");
        List<Object[]> llibresBiblio = Manager.findLlibresAmbBiblioteques();
        System.out.println(Manager.formatMultipleResult(llibresBiblio));
        */

        // ---------------------------------------------------------
        // TANCAMENT
        // Sempre tanquem la SessionFactory al finalitzar
        // ---------------------------------------------------------
        Manager.close();
        System.out.println("\nPrograma finalitzat.");
    }
}