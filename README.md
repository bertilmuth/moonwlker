# Moonwlker
[![Gitter](https://badges.gitter.im/requirementsascode/community.svg)](https://gitter.im/requirementsascode/community?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge)

Moonwlker is a facade for the Jackson JSON library.

It enables you to serialize and deserialize JSON objects without annotations in the classes.
Getting the Jackson annotations right is challenging, and Moonwlker does the configuration for you under the hood. This way, you can keep your classes free of JSON concerns.

On top of that, you can:
* (de)serialize immutable objects, without the need for a no-argument constructor or setters
* (de)serialize type hierarchies
* (de)serialize value types

## Getting started
Moonwlker is available on Maven Central.

If you are using Maven, include the following in your POM:

``` xml
<dependency>
  <groupId>org.requirementsascode</groupId>
  <artifactId>moonwlker</artifactId>
  <version>0.3-SNAPSHOT</version>
</dependency>
```

If you are using Gradle, include the following in your build.gradle:

```
implementation 'org.requirementsascode:moonwlker:0.3-SNAPSHOT'
```

At least Java 8 is required, download and install it if necessary.

## Basic usage and defaults
To create a Jackson `ObjectMapper` with Moonwlker, use this syntax:

``` java
import org.requirementsascode.moonwlker.MoonwlkerModule;
...
ObjectMapper objectMapper = new ObjectMapper();
objectMapper.registerModule(MoonwlkerModule.builder().build());
```
This creates an object mapper that ignores unknown properties when deserializing by default.

## All arguments constructor / immutable objects
The standard way in which Jackson supports all arguments constructors is to use the `@JsonCreator` and `@JsonProperties` annotations.
Moonwlker changes that: it enables you to deserialize objects that have a single, all arguments default constructor.

To enable this feature, you need to pass in the `-parameters` compiler argument when compiling your class files.
In Gradle, include this in your build file:

``` Groovy
gradle.projectsEvaluated {
 tasks.withType(JavaCompile) {
     options.compilerArgs << "-parameters"
 }
}
```

[This article](https://www.concretepage.com/java/jdk-8/java-8-reflection-access-to-parameter-names-of-method-and-constructor-with-maven-gradle-and-eclipse-using-parameters-compiler-argument#compiler-argument) describes how to do that in Maven and your IDE.

After you've done that, create an `ObjectMapper` as described in *Basic usage*.

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

## Integrate into Spring Boot application

To change the default `ObjectMapper` in a Spring Boot application, register the Moonwlker module as a bean:

``` java
@SpringBootApplication
public class GreeterApplication {
  public static void main(String[] args) {
    SpringApplication.run(GreeterApplication.class, args);
  }

  @Bean
  ObjectMapper objectMapper() {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(MoonwlkerModule.builder().build());    
    return objectMapper;
  } 
}
```

## (De)serialization of type hierarchies
Build your Jackson object mapper with Moonwlker like this:

``` java
ObjectMapper objectMapper = new ObjectMapper();

MoonwlkerModule module =
  MoonwlkerModule.builder()
    .fromProperty("type").toSubclassesOf(Person.class)
    .build();

objectMapper.registerModule(module);
```

In the above example, [Person](https://github.com/bertilmuth/moonwlker/blob/master/src/test/java/org/requirementsascode/moonwlker/testobject/person/Person.java) is the super class.
The created `ObjectMapper` (de)serializes objects of direct or indirect subclasses of that super class.
The `type` JSON property needs to specify the relative class name of the object to be created by Moonwlker (i.e. [Employee](https://github.com/bertilmuth/moonwlker/blob/master/src/test/java/org/requirementsascode/moonwlker/testobject/person/Employee.java)):

``` java
String jsonString = "{\"type\":\"Employee\",\"firstName\":\"Jane\",\"lastName\":\"Doe\",\"month\":\"OCTOBER\",\"employeeNumber\":\"EMP-2020\"}";
Employee employee = (Employee) objectMapper.readValue(jsonString, Object.class);
```
Use a simple class name like above if the sub class is in the same package as the super class.
Use a package prefix if the sub class is in a direct or indirect sub package of the super class' package. 
For example, this JSON String could be used if `Employee` was in the `company` subpackage of the package that `Person` is in:

``` java
String jsonString = "{\"type\":\"company.Employee\",\"firstName\":\"Jane\",\"lastName\":\"Doe\",\"month\":\"OCTOBER\",\"employeeNumber\":\"EMP-2020\"}";
```

You can also specify multiple base classes like so:

``` java
MoonwlkerModule module =
  MoonwlkerModule.builder()
    .fromProperty("type").toSubclassesOf(Animal.class, Person.class)
    .build();
```

See [this test class](https://github.com/bertilmuth/moonwlker/blob/master/src/test/java/org/requirementsascode/moonwlker/SubclassInSamePackageTest.java) for details on how to deserialize classes in the same package as their super class.

You can also define specific packages where subclasses can be found, like so:

``` java
MoonwlkerModule module = 
  MoonwlkerModule.builder()
    .fromProperty("type") 
    .toSubclassesOf(Person.class).in("org.requirementsascode.moonwlker.testobject.person")
    .toSubclassesOf(Animal.class).in("org.requirementsascode.moonwlker.testobject.animal")
      .build();
```

See [this test class](https://github.com/bertilmuth/moonwlker/blob/master/src/test/java/org/requirementsascode/moonwlker/SubclassInSpecifiedPackageTest.java) for details on how to deserialize classes in a specified package.

## Custom (de)serialization of value types
Sometimes you may want to customize the (de)serialization of a value type. 

In this context, value types are wrappers around scalar types like Strings, Integer and so on. 

Instead of serializing a value type instance to a JSON object, you rather want a plain String. 

Let's have a look at two example classes:

``` java
public class ObjectWithJsonValue {
    private final String someString;
    private final OrphanAnimal orphanAnimal;

    public ObjectWithJsonValue(String someString, OrphanAnimal orphanAnimal) {
        this.someString = someString;
        this.orphanAnimal = orphanAnimal;
    }

    public String getSomeString() {
        return someString;
    }

    public OrphanAnimal getOrphanAnimal() {
        return orphanAnimal;
    }
}
....
public class OrphanAnimal{
	private final String name;

	public OrphanAnimal(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
```

Say you create an instance of `ObjectWithJsonValue` and serialize it:

``` java
ObjectWithJsonValue object = new ObjectWithJsonValue("TestString", new OrphanAnimal("PaulTheDog"));
String json = objectMapper.writeValueAsString(object);
```

If you do this, without custom serialization the `json` String looks like this:
`{"someString":"TestObject","orphanAnimal":{"name":"PaulTheDog"}}`

As you can see, the orphan animal instance is serialized as a JSON object.

If you enable custom serialization, the orphan animal instance is instead "flattened" to a plain String:
`{"someString":"TestString","orphanAnimal":"PaulTheDog"}`

To enable Moonwlker to serialize a value type, you need to provide a function that converts the
value type instance to a String and vice versa:

``` java
MoonwlkerModule module = MoonwlkerModule.builder()
  .addValueType(OrphanAnimal.class, OrphanAnimal::getName, OrphanAnimal::new)
  .build();
```

The first argument is the value type's class. 

The second argument is a function that converts an instance of the value type to a String.
Moonwlker uses it for serialization.

The third argument is the opposite of the second: it's a function that converts a String to a new value type instance.
Moonwlker uses it for deserialization.


