package org.requirementsascode.moonwlker;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.requirementsascode.moonwlker.testobject.animal.Animal;
import org.requirementsascode.moonwlker.testobject.animal.Dog;
import org.requirementsascode.moonwlker.testobject.person.Employee;
import org.requirementsascode.moonwlker.testobject.person.Person;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidTypeIdException;

public class SubclassInSamePackageTest extends MoonwlkerModuleTest {
  /*
   * Happy path tests
   */

  @Test
  public void readsAndWrites_oneObject_withHierarchy() throws Exception {
    ObjectMapper objectMapper = new ObjectMapper();
    MoonwlkerModule module = MoonwlkerModule.builder()
        .fromProperty("type").toSubclassesOf(Person.class)
        .build();
    objectMapper.registerModule(module);

    String jsonString = "{\"type\":\"Employee\",\"firstName\":\"Jane\",\"lastName\":\"Doe\",\"employeeNumber\":\"EMP-2020\"}";
    Person person = objectMapper.readValue(jsonString, Person.class);
    assertEquals("Jane", person.firstName());
    assertEquals("Doe", person.lastName());
    assertEquals("EMP-2020", ((Employee) person).employeeNumber());

    assertEquals(jsonString, writeToJson(objectMapper, person));
  }

  @Test
  public void readsAndWrites_oneObject_withDifferentTypeProperty() throws Exception {
    ObjectMapper objectMapper = new ObjectMapper();
    MoonwlkerModule module = MoonwlkerModule.builder()
        .fromProperty("kind").toSubclassesOf(Person.class)
        .build();
    objectMapper.registerModule(module);

    String jsonString = "{\"kind\":\"Employee\",\"firstName\":\"Jane\",\"lastName\":\"Doe\",\"employeeNumber\":\"EMP-2020\"}";
    Person person = objectMapper.readValue(jsonString, Person.class);
    assertEquals("Jane", person.firstName());
    assertEquals("Doe", person.lastName());
    assertEquals("EMP-2020", ((Employee) person).employeeNumber());

    assertEquals(jsonString, writeToJson(objectMapper, person));
  }

  @Test
  public void readsAndWrites_oneObject_withSingleArgumentConstructor() throws Exception {
    ObjectMapper objectMapper = new ObjectMapper();
    MoonwlkerModule module = MoonwlkerModule.builder()
        .fromProperty("kind").toSubclassesOf(Animal.class)
        .build();
    objectMapper.registerModule(module);

    String jsonString = "{\"kind\":\"UnspecificAnimal\",\"price\":23}";
    Animal animal = objectMapper.readValue(jsonString, Animal.class);
    assertEquals(new BigDecimal(23), animal.price());

    assertEquals(jsonString, writeToJson(objectMapper, animal));
  }

  @Test
  public void readsAndWrites_twoObjects() throws Exception {
    ObjectMapper objectMapper = new ObjectMapper();
    MoonwlkerModule module = MoonwlkerModule.builder()
        .fromProperty("type").toSubclassesOf(Animal.class, Person.class)
        .build();
    objectMapper.registerModule(module);

    String jsonString = "{\"type\":\"Dog\",\"price\":412,\"name\":\"Calla\",\"command\":\"Sit\"}";
    Dog dog = (Dog) objectMapper.readValue(jsonString, Animal.class);
    assertEquals("Calla", dog.name());
    assertEquals("Sit", dog.command());
    assertEquals(jsonString, writeToJson(objectMapper, dog));

    jsonString = "{\"type\":\"Employee\",\"firstName\":\"Jane\",\"lastName\":\"Doe\",\"employeeNumber\":\"EMP-2020\"}";
    Employee employee = (Employee) objectMapper.readValue(jsonString, Person.class);
    assertEquals("Jane", employee.firstName());
    assertEquals("Doe", employee.lastName());
    assertEquals("EMP-2020", employee.employeeNumber());
    assertEquals(jsonString, writeToJson(objectMapper, employee));
  }

  /*
   * Error path tests
   */

  @Test
  public void doesntRead_objectThatIsntSubclass() throws Exception {
    ObjectMapper objectMapper = new ObjectMapper();
    MoonwlkerModule module = MoonwlkerModule.builder()
        .fromProperty("type").toSubclassesOf(Animal.class)
        .build();
    objectMapper.registerModule(module);

    String jsonString = "{\"type\":\"OrphanAnimal\",\"name\":\"Toad\"\"}";
    assertThrows(InvalidTypeIdException.class, () -> objectMapper.readValue(jsonString, Animal.class));
  }

  @Test
  public void doesntRead_objectInWrongPackage() throws Exception {
    ObjectMapper objectMapper = new ObjectMapper();
    MoonwlkerModule module = MoonwlkerModule.builder()
        .fromProperty("type").toSubclassesOf(Animal.class, Person.class)
        .build();
    objectMapper.registerModule(module);

    String jsonString = "{\"type\":\"StrayCat\",\"price\":1,\"name\":\"Bella\",\"nickname\":\"Bee\"}";
    assertThrows(InvalidTypeIdException.class, () -> objectMapper.readValue(jsonString, Animal.class));

  }
}
