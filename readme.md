# EasyCrud – Générateur CRUD Jakarta/Spring 6

Annotation processors qui génèrent automatiquement contrôleurs REST, services, repositories et mappers pour vos entités métier. L’objectif : se concentrer sur la logique métier, pas sur le code répétitif CRUD/pagination.

## Stack actuelle
- Java 21
- Spring Framework 6.1 / Spring Data JPA 3.3 / Spring Security 6.3
- Jakarta Persistence & Validation (jakarta.\*)
- OpenAPI v3 via `springdoc-openapi-starter-webmvc`
- MapStruct 1.6 pour les conversions DTO
- Pas de Lombok requis

## Comment ça marche ?
1) Vous déclarez vos abstractions :  
```java
@EasyReadController(entity = Pet.class, dto = PetDto.class, id = Long.class, user = User.class)
public interface PetController { }

@EasyReadService(entity = Pet.class, id = Long.class, user = User.class)
public abstract class PetService { }

@EasyRepository(entity = Pet.class, id = Long.class)
public interface PetRepository { }

@EasyReadMapper(entity = Pet.class, dto = PetDto.class, user = User.class)
public interface PetMapper { }
```
2) À la compilation, l’annotation processor génère :
   - `PetControllerImpl` (REST, endpoints GET by id + pagination)
   - `PetServiceImpl` (implémente `BaseReadService`)
   - `IPetRepository` (extends `BaseRepository`)
   - `PetMapperImpl` (MapStruct)
3) Vous branchez vos impls/custom extensions si besoin : ajoutez vos méthodes à l’interface/abstract d’origine, elles cohabitent avec le code généré.

## Quickstart Maven
Dans votre POM :
```xml
<properties>
  <java.version>21</java.version>
  <easycrud.version>1.0.0-SNAPSHOT</easycrud.version>
</properties>

<dependencies>
  <dependency>
    <groupId>fr.vvlabs.easycrud</groupId>
    <artifactId>easycrud-commonns</artifactId>
    <version>${easycrud.version}</version>
  </dependency>
</dependencies>

<build>
  <plugins>
    <plugin>
      <groupId>org.apache.maven.plugins</groupId>
      <artifactId>maven-compiler-plugin</artifactId>
      <configuration>
        <annotationProcessorPaths>
          <path>
            <groupId>fr.vvlabs.easycrud</groupId>
            <artifactId>easycrud-processor</artifactId>
            <version>${easycrud.version}</version>
          </path>
        </annotationProcessorPaths>
      </configuration>
    </plugin>
  </plugins>
</build>
```

## Modules du repo
- `easycrud-core` : abstractions (BaseReadController, BaseReadService, BaseRepository, annotations Easy*), config OpenAPI.
- `easycrud-processor` : annotation processors JavaPoet qui génèrent les implémentations.
- `easycrud-test` : projet démo + tests JUnit5, contexte Spring/H2 en mémoire pour valider le CRUD généré.

## Tests
- Unitaires : valident que les classes générées implémentent les bons contrats.
- IT : démarre un contexte Spring Data JPA sur H2 (`SpringContextIT`) et vérifie un cycle CRUD complet.
Lancer : `mvn test` (H2 embarqué, rien à installer).

## Ce que vous obtenez
- Endpoints REST OpenAPI v3 (springdoc) prêts à l’emploi.
- Génération Spring-native mais code métier découplé : toutes les entités doivent implémenter `EasyEntity`; les DTO/Users sont libres.
- Extensible : ajoutez vos méthodes custom dans les interfaces/abstracts, ou étendez les impls générées.

## Roadmap courte
- Support écriture (CREATE/UPDATE/DELETE) via `@EasyWrite*` (à compléter/étendre).
- Génération d’exemples OpenAPI et filtres de sécurité personnalisables.
