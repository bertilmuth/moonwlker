package org.requirementsascode.moonwlker;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.requirementsascode.moonwlker.Moonwlker;
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
        Moonwlker.objectMapperBuilder()
          .subclassesOf(Animal.class).inPackage("org.requirementsascode.moonwlker.testobject.person")
          .subclassesOf(Person.class).inPackage("")
            .build();
    
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
