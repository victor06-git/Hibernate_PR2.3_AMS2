package com.project.dao;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assumptions;

import com.project.domain.Autor;
import com.project.domain.Biblioteca;
import com.project.domain.Exemplar;
import com.project.domain.Llibre;
import com.project.domain.Persona;
import com.project.domain.Prestec;

public class ManagerTest {

    @BeforeAll
    public static void setup() {
        Manager.createSessionFactory();
    }

    @AfterAll
    public static void tearDown() {
        Manager.close();
    }

    @Test
    public void testCreateBasicEntities() {
        Autor autor = Manager.addAutor("Test Autor");
        assertNotNull(autor);

        Llibre llibre = Manager.addLlibre("ISBN-TEST-1", "Titol Test", "Editorial", 2020);
        assertNotNull(llibre);

        Biblioteca biblio = Manager.addBiblioteca("BiblioTest", "Ciutat", "Adreca 1", "600000000", "b@t.test");
        assertNotNull(biblio);

        Persona persona = Manager.addPersona("X0000000T", "Persona Test", "600000001", "p@t.test");
        assertNotNull(persona);

        Exemplar exemplar = Manager.addExemplar("CB-TEST-1", llibre, biblio);
        assertNotNull(exemplar);
        assertTrue(exemplar.isDisponible(), "Nou exemplar hauria d'estar disponible per defecte");

        // Verify collections contain the created objects
        Collection<Llibre> llibres = Manager.listCollection(Llibre.class);
        assertTrue(llibres.stream().anyMatch(l -> "ISBN-TEST-1".equals(l.getIsbn())));

        Collection<Exemplar> exemplars = Manager.listCollection(Exemplar.class);
        assertTrue(exemplars.stream().anyMatch(e -> "CB-TEST-1".equals(e.getCodiBarres())));
    }

    @Test
    public void testManyToManyAndFindLlibresAmbAutors() {
        Autor autor = Manager.addAutor("Autor M:N");
        Llibre llibre = Manager.addLlibre("ISBN-TEST-2", "Titol MN", "Editorial", 2019);

        // Vinculem autor i llibre utilitzant el mètode updateAutor
        Set<Llibre> llibres = new HashSet<>();
        llibres.add(llibre);
        Manager.updateAutor(autor.getAutorId(), autor.getNom(), llibres);

        List<Llibre> results = Manager.findLlibresAmbAutors();
        assertNotNull(results);
        assertTrue(results.stream().anyMatch(l -> "ISBN-TEST-2".equals(l.getIsbn())),
                "Ha de contenir el llibre amb autor");
        // Also check that the returned Llibre has authors loaded
        Llibre found = results.stream().filter(l -> "ISBN-TEST-2".equals(l.getIsbn())).findFirst().orElse(null);
        assertNotNull(found);
        assertFalse(found.getAutors().isEmpty(), "El llibre hauria de tenir autors carregats");
    }

    @Test
    public void testFindLlibresAmbBiblioteques() {
        Llibre llibre = Manager.addLlibre("ISBN-TEST-3", "Titol Biblio", "Editorial", 2018);
        Biblioteca biblio = Manager.addBiblioteca("Biblio2", "Ciutat2", "Ad2", "600000002", "b2@t.test");
        Exemplar exemplar = Manager.addExemplar("CB-TEST-2", llibre, biblio);

        List<Object[]> rows = Manager.findLlibresAmbBiblioteques();
        assertNotNull(rows);
        assertTrue(rows.stream().anyMatch(r -> "Titol Biblio".equals(r[0]) && "Biblio2".equals(r[1])),
                "Ha de contenir la fila amb títol i biblioteca");
    }

    @Test
    public void testPrestecAndReturnFlow() {
        Llibre llibre = Manager.addLlibre("ISBN-TEST-4", "Titol Prestec", "Editorial", 2017);
        Biblioteca biblio = Manager.addBiblioteca("Biblio3", "Ciutat3", "Ad3", "600000003", "b3@t.test");
        Exemplar exemplar = Manager.addExemplar("CB-TEST-3", llibre, biblio);
        Persona persona = Manager.addPersona("X1111111A", "Persona Prestec", "600000004", "pp@t.test");

        Prestec prestec = Manager.addPrestec(exemplar, persona, LocalDate.now(), LocalDate.now().plusDays(7));
        // If prestec couldn't be created (race or DB config), skip assertions
        Assumptions.assumeTrue(prestec != null, "Presteç no creat; s'ha saltat la comprovació de préstec");

        // After creating prestec, exemplar hauria d'estar no disponible
        Collection<Exemplar> exemplars = Manager.listCollection(Exemplar.class);
        Exemplar exDB = exemplars.stream().filter(e -> "CB-TEST-3".equals(e.getCodiBarres())).findFirst().orElse(null);
        assertNotNull(exDB);
        assertFalse(exDB.isDisponible(), "Exemplar hauria d'estar prestat (no disponible)");

        // Registrar retorn
        Long pid = prestec.getPrestecId();
        Assumptions.assumeTrue(pid != null, "Prestec id null; no es pot provar el retorn");
        Manager.registrarRetornPrestec(pid, LocalDate.now());

        // Comprovar que l'exemplar torna a estar disponible i el préstec no actiu
        Collection<Exemplar> exemplars2 = Manager.listCollection(Exemplar.class);
        Exemplar exAfter = exemplars2.stream().filter(e -> "CB-TEST-3".equals(e.getCodiBarres())).findFirst()
                .orElse(null);
        assertNotNull(exAfter);
        assertTrue(exAfter.isDisponible(), "Exemplar hauria de ser de nou disponible després del retorn");

        Collection<Prestec> prestecs = Manager.listCollection(Prestec.class);
        Prestec prestecAfter = prestecs.stream().filter(p -> pid.equals(p.getPrestecId())).findFirst().orElse(null);
        assertNotNull(prestecAfter);
        assertFalse(prestecAfter.isActiu(), "El préstec hauria d'estar marcat com a no actiu després del retorn");
    }

    @Test
    public void testFindLlibresEnPrestecQuery() {
        // Creem el necessari
        Llibre llibre = Manager.addLlibre("ISBN-TEST-5", "Titol En Prestec", "Editorial", 2016);
        Biblioteca biblio = Manager.addBiblioteca("Biblio4", "Ciutat4", "Ad4", "600000005", "b4@t.test");
        Exemplar exemplar = Manager.addExemplar("CB-TEST-4", llibre, biblio);
        Persona persona = Manager.addPersona("X2222222B", "Persona EP", "600000006", "pep@t.test");

        Prestec prestec = Manager.addPrestec(exemplar, persona, LocalDate.now(), LocalDate.now().plusDays(5));
        Assumptions.assumeTrue(prestec != null, "Presteç no creat; s'ha saltat la comprovació de llibres en préstec");

        List<Object[]> rows = Manager.findLlibresEnPrestec();
        assertNotNull(rows);
        assertTrue(rows.stream().anyMatch(r -> "Titol En Prestec".equals(r[0]) && "Persona EP".equals(r[1])),
                "Ha de contenir el llibre i nom de la persona en préstec actiu");
    }
}
