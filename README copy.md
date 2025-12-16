# Punt de partida PR2.3 Hibernate (Gestió de Biblioteca)

En aquest projecte hi ha el codi base per realitzar la pràctica de gestió d'una biblioteca utilitzant Hibernate i JPA. L'objectiu és implementar les anotacions a les entitats i la lògica del `Manager`.

## Organització del projecte

### `com.project.domain`
Conté les classes del domini que s'han de convertir en Entitats JPA.
* **Autor:** Autors dels llibres.
* **Llibre:** Informació dels llibres (ISBN, títol...).
* **Biblioteca:** Ubicació física.
* **Exemplar:** Còpia física d'un llibre.
* **Persona:** Usuaris de la biblioteca.
* **Prestec:** Registre dels préstecs realitzats.

### `com.project.dao`
Conté la classe `Manager` on s'ha d'implementar la lògica d'accés a dades (CRUD i consultes).

### `com.project.sqliteutils`
Utilitats addicionals per inspeccionar la base de dades SQLite directament sense passar per Hibernate.

---

## Compilació i execució

Cal el 'Maven' per compilar el projecte.

### Neteja i compilació bàsica
```bash
mvn clean
mvn compile
```

### Execució amb script (Recomanat)
Aquests scripts configuren automàticament les opcions de Java necessàries (`--add-opens`).

Per executar el projecte a Windows:
```bash
.\run.ps1 com.project.Main
```

Per executar el projecte a Linux/macOS:
```bash
./run.sh com.project.Main
```

### Altres mètodes d'execució
Si prefereixes executar el projecte pas a pas o tens problemes amb els scripts, pots seguir les següents instruccions:

#### 1. Execució amb Maven (mvn exec)
Executa la classe principal directament des de Maven:
```bash
mvn exec:java -q "-Dexec.mainClass=com.project.Main"
```

Si volguessis executar la utilitat de SQLite per veure les taules creades:
```bash
mvn exec:java -q "-Dexec.mainClass=com.project.sqliteutils.MainSQLite"
```

#### 2. Execució directa amb Java (classpath)
Primer, prepara les dependències en una carpeta local:
```bash
mvn clean package dependency:copy-dependencies
```

Després, executa el programa afegint les llibreries al classpath:
```bash
java --add-opens java.base/java.lang=ALL-UNNAMED --add-opens java.base/java.nio=ALL-UNNAMED --add-opens java.base/java.util=ALL-UNNAMED -cp "target/classes;target/dependency/*" com.project.Main
```

#### 3. Execució directa amb Java (jar package)
Si has generat el JAR amb `mvn package`, pots executar-lo així (assegura't del nom correcte del fitxer generat a la carpeta target):
```bash
java --add-opens java.base/java.lang=ALL-UNNAMED --add-opens java.base/java.nio=ALL-UNNAMED --add-opens java.base/java.util=ALL-UNNAMED -jar target/mp0486-tema2-pr23-1.0.0
```

### Execució de tests, si n'hi hagués
El projecte està preparat per executar tests unitaris amb JUnit 5.
```bash
# Executar TOTS els tests
mvn test

# Executar un test individual especificant la classe
mvn test -Dtest=ManagerTest

# Executar múltiples tests específics (separats per comes)
mvn test -Dtest="AutorTest,LlibreTest"

# Executar tots els tests que comencin per un prefix (wildcard)
mvn test -Dtest="Prestec*"
```

---

## Base de Dades

### SQLite (Per defecte)
El projecte està configurat per defecte per utilitzar SQLite.
* El fitxer de base de dades es generarà automàticament a: `./data/database-pr23.db`.
* No cal instal·lar res extra.


## Tasques a realitzar (Resum)
1. **Entitats (`com.project.domain`):** Afegir les anotacions JPA (`@Entity`, `@Id`, `@OneToMany`, `@ManyToOne`, `@ManyToMany`) a totes les classes.
2. **Manager (`com.project.dao`):** Implementar els mètodes que estan marcats amb `TODO` per realitzar la persistència i les consultes.
3. **Main (`com.project.Main`):** Descomentar progressivament el codi per provar que les funcionalitats implementades funcionen correctament.