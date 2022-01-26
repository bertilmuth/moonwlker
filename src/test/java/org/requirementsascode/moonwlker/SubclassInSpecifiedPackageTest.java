package org.requirementsascode.moonwlker;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import org.junit.jupiter.api.Test;
import org.requirementsascode.moonwlker.testobject.animal.Animal;
import org.requirementsascode.moonwlker.testobject.animal.Cat;
import org.requirementsascode.moonwlker.testobject.person.Employee;
import org.requirementsascode.moonwlker.testobject.person.Person;

import com.fasterxml.jackson.databind.ObjectMapper;

public class SubclassInSpecifiedPackageTest extends MoonwlkerModuleTest{
  /*
   * Happy path tests
   */

  @Test
  public void readsAndWrites_twoObjects_inSpecifiedPackage() throws Exception {
    ObjectMapper objectMapper = new ObjectMapper();
    MoonwlkerModule module = MoonwlkerModule.builder()
        .fromProperty("type")
        .toSubclassesOf(Person.class).in("org.requirementsascode.moonwlker.testobject.person")
        .toSubclassesOf(Animal.class).in("org.requirementsascode.moonwlker.testobject.animal")
          .build();
    objectMapper.registerModule(module)
        .setSerializationInclusion(Include.NON_NULL);

    String jsonString = "{\"type\":\"Cat\",\"price\":1,\"name\":\"Bella\",\"nickname\":\"Bee\"}";
    Cat cat = (Cat) objectMapper.readValue(jsonString, Animal.class);
    assertEquals("Bella", cat.name());
    assertEquals("Bee", cat.nickname());
    assertEquals(jsonString, writeToJson(objectMapper, cat));

    jsonString = "{\"type\":\"Employee\",\"firstName\":\"John\",\"lastName\":\"Public\",\"employeeNumber\":\"EMP-0815\"}";
    Employee employee = (Employee) objectMapper.readValue(jsonString, Person.class);
    assertEquals("John", employee.firstName());
    assertEquals("Public", employee.lastName());
    assertEquals("EMP-0815", employee.employeeNumber());
  }

  @Test
  public void readsAndWrites_objects_inDefaultPackage() throws Exception {
    ObjectMapper objectMapper = new ObjectMapper();
    MoonwlkerModule module =
        MoonwlkerModule.builder()
          .fromProperty("type") .toSubclassesOf(Person.class).in("")
          .build();
    objectMapper.registerModule(module);

    String jsonString = "{\"type\":\"LostEmployee\",\"firstName\":\"John\",\"lastName\":\"Public\",\"employeeNumber\":\"EMP-0815\"}";
    Employee employee = (Employee) objectMapper.readValue(jsonString, Person.class);
    assertEquals("John", employee.firstName());
    assertEquals("Public", employee.lastName());
    assertEquals("EMP-0815", employee.employeeNumber());
  }
}
