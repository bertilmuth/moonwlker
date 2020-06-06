package org.requirementsascode.moonwlker;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.requirementsascode.moonwlker.Moonwlker.map;

import org.junit.jupiter.api.Test;
import org.requirementsascode.moonwlker.testobject.animal.Animal;
import org.requirementsascode.moonwlker.testobject.animal.Cat;
import org.requirementsascode.moonwlker.testobject.person.Employee;
import org.requirementsascode.moonwlker.testobject.person.Person;

import com.fasterxml.jackson.databind.ObjectMapper;

public class SubclassInSpecifiedPackageTest extends MoonwlkerTest{
  /*
   * Happy path tests
   */
  
  @Test 
  public void readsAndWrites_twoObjects_inSpecifiedPackage() throws Exception {
    ObjectMapper objectMapper = 
        map()
          .to(Animal.class).instancesIn("org.requirementsascode.moonwlker.testobject.person")
          .to(Person.class).instancesIn("")
            .withSimpleName();
    
    String jsonString = "{\"type\":\"StrayCat\",\"price\":1,\"name\":\"Bella\",\"nickname\":\"Bee\"}";
    Cat cat = (Cat) objectMapper.readValue(jsonString, Animal.class);
    assertEquals("Bella", cat.name);
    assertEquals("Bee", cat.nickname);
    assertEquals(jsonString, writeToJson(objectMapper, cat));
    
    jsonString = "{\"type\":\"LostEmployee\",\"firstName\":\"John\",\"lastName\":\"Public\",\"employeeNumber\":\"EMP-0815\"}";
    Employee employee = (Employee) objectMapper.readValue(jsonString, Person.class);
    assertEquals("John", employee.getFirstName());
    assertEquals("Public", employee.getLastName());
    assertEquals("EMP-0815", employee.getEmployeeNumber());
  }
}