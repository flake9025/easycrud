# EasyCrud - Java CRUD, the easy way!

## What is EasyCrud ?

EasyCrud is a Java [annotation processor](http://docs.oracle.com/javase/6/docs/technotes/guides/apt/index.html) for the generation of Java OpenAPI 3.0 REST Services.
It will help you focus on business logic instead of writing repetitive code for CRUD, Filtering, Paging and Sorting operations

The idea is simple : this project is inspired by [Lombok](https://github.com/rzwitserloot/lombok) and [MapStruct](https://github.com/mapstruct/mapstruct) frameworks and tries to provide simple and automated logics.
It is based on an old project [stack-helper](https://github.com/flake9025/stack-helper), re-designed with modern and easy-understanding annotations.

Furthermore, this CRUD will give you an easy way to plug in your security access rules on objects, based on entities and users properties.

To create a simple CRUD read-only web-service, declare an interface or abstract class like this:

```java
@EasyReadController(entity = Pet.class, dto = PetDto.class, id = Long.class, user = User.class)
public interface PetController {
  // add custom code / endpoints if needed
}
```

```java
@EasyReadController(entity = Pet.class, dto = PetDto.class, id = Long.class, user = User.class)
public abstract class PetController {
  // add custom code / endpoints if needed
}
```

You just have to provide the classes you use as Entity, DTO, the ID (primary key), and User types. 

At compile time EasyCrud will generate an implementation of the interface, with the following endpoints :
- GET by ID
- FIND with filtering and pagination and sorting
- COUNT with filtering and pagination and sorting
- SEARCH with filtering and pagination and sorting

To create a simple CRUD read & write web-service, declare an interface or abstract class like this:

```java
@EasyWriteController(entity = Pet.class, dto = PetDto.class, postDto = PetPostDto.class, id = Long.class, user = User.class)
public interface PetController {
  // add custom code / endpoints if needed
}
```

```java
@EasyWriteController(entity = Pet.class, dto = PetDto.class, postDto = PetPostDto.class, id = Long.class, user = User.class)
public abstract class PetController {
  // add custom code / endpoints if needed
}
```

At compile time EasyCrud will generate an implementation of the interface, with the following endpoints :
- all from the READ annotation
- CREATE
- UPDATE by ID
- UPDATE list
- DELETE by ID
- DELETE list by ID
- DELETE all

## Requirements

EasyCrud requires Java 11 or later.
It is designed for :
- Spring 5.14
- Hibernate 5.3
- MapStruct 1.3.1
- Lombok 1.18.10

## Using EasyCrud


### Maven

For Maven-based projects, add the following to your POM file in order to use MapStruct (the dependencies are available at Maven Central):

```xml
...
<properties>
    <fr.vvlabs.easycrud.version>1.0.0</fr.vvlabs.easycrud.version>
</properties>
...
<dependencies>
    <dependency>
        <groupId>fr.vvlabs.easycrud</groupId>
        <artifactId>easycrud</artifactId>
        <version>${fr.vvlabs.easycrud.version}</version>
    </dependency>
</dependencies>
...
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
                        <version>${fr.vvlabs.easycrud.version}</version>
                    </path>
                </annotationProcessorPaths>
            </configuration>
        </plugin>
    </plugins>
</build>
...
```