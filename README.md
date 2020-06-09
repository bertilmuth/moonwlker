# Moonwlker
[![Build Status](https://travis-ci.org/bertilmuth/moonwlker.svg?branch=master)](https://travis-ci.org/bertilmuth/moonwlker)

Moonwlker is a facade for the Jackson JSON library.

It enables you to serialize and deserialize JSON objects without annotations in the classes.

Right now, you can (de)serialize objects with an all arguments constructor, and type hierarchies.

This is helpful if you don't have access to the classes, or don't want to annotate them to keep them free of JSON concerns.

*This project is in an early stage. The API may change.*

# Getting started
Moonwlker is available on Maven Central.

If you are using Maven, include the following in your POM:

``` xml
<dependency>
  <groupId>org.requirementsascode</groupId>
  <artifactId>moonwlker</artifactId>
  <version>0.0.1</version>
</dependency>
```

If you are using Gradle, include the following in your build.gradle:

```
implementation 'org.requirementsascode:moonwlker:0.0.1'
```

At least Java 8 is required, download and install it if necessary.

# All arguments constructor / immutable objects
Be default, Jackson supports all arguments constructors only if you use the `@JsonCreator` and `@JsonProperties` annotation.
Moonwlker changes that: it enables you to deserialize objects that have a single, all arguments default constructor.

To enable this feature, you need to pass in the `-parameters` compiler argument when compiling your class files.
[This article](https://www.concretepage.com/java/jdk-8/java-8-reflection-access-to-parameter-names-of-method-and-constructor-with-maven-gradle-and-eclipse-using-parameters-compiler-argument#compiler-argument) describes how to do that.

After you've done that, to use this Moonwlker feature, import Moonwlker and create an `ObjectMapper` like in the following example:

``` java
import static org.requirementsascode.moonwlker.Moonwlker.json;
...
ObjectMapper objectMapper = 
    json().mapper();

String jsonString = "{\"price\":412,\"name\":\"Calla\",\"command\":\"Sit\"}";
Dog dog = objectMapper.readValue(jsonString, Dog.class);
```

Here's what the example [Dog class](https://github.com/bertilmuth/moonwlker/blob/master/src/test/java/org/requirementsascode/moonwlker/testobject/animal/Dog.java) looks like:

``` java
public class Dog extends Animal {
  private final String name;
  private final String command;

  public Dog(BigDecimal price, String name, String command) {
    super(price);
    this.name = name;
    this.command = command;
  }
  
  public String name() {
    return name;
  }

  public String command() {
    return command;
  }
}
```

See [this test class](https://github.com/bertilmuth/moonwlker/blob/master/src/test/java/org/requirementsascode/moonwlker/GeneralTest.java) for details on how to deserialize objects with an all arguments constructor.

Normally, Jackson has special behavior for single argument constructors.
Moonwlker changes that: it treats single argument constructors the same to simplify deserialization.

# (De)serialization of type hierarchies
Build your Jackson object mapper with Moonwlker:

``` java
import static org.requirementsascode.moonwlker.Moonwlker.*;
...
ObjectMapper objectMapper = 
    json("type").to(Person.class).mapper();
```

In the above example, [Person](https://github.com/bertilmuth/moonwlker/blob/master/src/test/java/org/requirementsascode/moonwlker/testobject/person/Person.java) is the base class.
The created `ObjectMapper` deserializes objects of subclasses in the same package.
The `type` JSON property specifies the simple class name of the object to be created by Moonwlker (i.e. [Employee](https://github.com/bertilmuth/moonwlker/blob/master/src/test/java/org/requirementsascode/moonwlker/testobject/person/Employee.java)):

``` java
String jsonString = "{ \"type\" : \"Employee\", \"firstName\" : \"Jane\", \"lastName\" : \"Doe\" , \"employeeNumber\" : \"EMP-2020\"}";
Employee employee = (Employee) objectMapper.readValue(jsonString, Person.class);
```

You can also specify multiple base classes like so:

``` java
ObjectMapper objectMapper = 
    json("kind").to(Animal.class, Person.class).mapper();

String jsonString = "{\"kind\":\"Dog\",\"price\":412,\"name\":\"Calla\",\"command\":\"Sit\"}";
Dog dog = (Dog) objectMapper.readValue(jsonString, Animal.class);
jsonString = "{\"kind\":\"Employee\",\"firstName\":\"Jane\",\"lastName\":\"Doe\",\"employeeNumber\":\"EMP-2020\"}";
Employee employee = (Employee) objectMapper.readValue(jsonString, Person.class);
```

See [this test class](https://github.com/bertilmuth/moonwlker/blob/master/src/test/java/org/requirementsascode/moonwlker/SubclassInSamePackageTest.java) for details on how to deserialize classes in the same package as their super class.

You can also define specific packages where subclasses can be found, like so:

``` java
ObjectMapper objectMapper = 
    json("type")
      .to(Animal.class).in("org.requirementsascode.moonwlker.testobject.person")
      .to(Person.class).in("")
        .mapper();
```

See [this test class](https://github.com/bertilmuth/moonwlker/blob/master/src/test/java/org/requirementsascode/moonwlker/SubclassInSpecifiedPackageTest.java) for details on how to deserialize classes in a specified package.
