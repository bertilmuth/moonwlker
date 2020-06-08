# Moonwlker
[![Build Status](https://travis-ci.org/bertilmuth/moonwlker.svg?branch=master)](https://travis-ci.org/bertilmuth/moonwlker)

Moonwlker is a facade for the Jackson JSON library.

The goal is to enable serializing and deserializing JSON objects without annotations in the classes.

This is helpful if you don't have access to the classes, or don't want to annotate them to keep them free of JSON concerns.

*This project is in an early stage. The API may change.*

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
