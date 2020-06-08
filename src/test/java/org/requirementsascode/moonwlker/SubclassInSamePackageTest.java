package org.requirementsascode.moonwlker;

import static org.requirementsascode.moonwlker.Moonwlker.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.requirementsascode.moonwlker.testobject.animal.Animal;
import org.requirementsascode.moonwlker.testobject.animal.Dog;
import org.requirementsascode.moonwlker.testobject.person.Employee;
import org.requirementsascode.moonwlker.testobject.person.Person;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidTypeIdException;

public class SubclassInSamePackageTest extends MoonwlkerTest{
  /*
   * Happy path tests 
   */
  
  @Test 
  public void readsAndWrites_oneObject() throws Exception {
    ObjectMapper objectMapper = 
        json("type").to(Person.class).mapper();
    
    String jsonString = "{\"type\":\"Employee\",\"firstName\":\"Jane\",\"lastName\":\"Doe\",\"employeeNumber\":\"EMP-2020\"}";
    Person person = objectMapper.readValue(jsonString, Person.class);
    assertEquals("Jane", person.getFirstName());
    assertEquals("Doe", person.getLastName());
    assertEquals("EMP-2020", ((Employee)person).getEmployeeNumber());       
    
    assertEquals(jsonString, writeToJson(objectMapper, person));
  }
  
  @Test 
  public void readsAndWrites_oneObject_withDifferentTypeProperty() throws Exception {
    ObjectMapper objectMapper = 
        json("kind").to(Person.class).mapper();
    
    String jsonString = "{\"kind\":\"Employee\",\"firstName\":\"Jane\",\"lastName\":\"Doe\",\"employeeNumber\":\"EMP-2020\"}";
    Person person = objectMapper.readValue(jsonString, Person.class);
    assertEquals("Jane", person.getFirstName());
    assertEquals("Doe", person.getLastName());
    assertEquals("EMP-2020", ((Employee)person).getEmployeeNumber());
        
    assertEquals(jsonString, writeToJson(objectMapper, person));
  }
  
  @Test 
  public void readsAndWrites_twoObjects() throws Exception {
    ObjectMapper objectMapper = 
        json("type").to(Animal.class, Person.class).mapper();
    
    String jsonString = "{\"type\":\"Dog\",\"price\":412,\"name\":\"Calla\",\"command\":\"Sit\"}";
    Dog dog = (Dog) objectMapper.readValue(jsonString, Animal.class);
    assertEquals("Calla", dog.name());
    assertEquals("Sit", dog.command());
    assertEquals(jsonString, writeToJson(objectMapper, dog));
    
    jsonString = "{\"type\":\"Employee\",\"firstName\":\"Jane\",\"lastName\":\"Doe\",\"employeeNumber\":\"EMP-2020\"}";
    Employee employee = (Employee) objectMapper.readValue(jsonString, Person.class);
    assertEquals("Jane", employee.getFirstName());
    assertEquals("Doe", employee.getLastName());
    assertEquals("EMP-2020", employee.getEmployeeNumber());
    assertEquals(jsonString, writeToJson(objectMapper, employee));
  }
  
  /*
   * Error path tests
   */
  
  @Test
  public void doesntRead_objectThatIsntSubclass() throws Exception {
    ObjectMapper objectMapper = 
        json("type").to(Animal.class).mapper();
    
    String jsonString = "{\"type\":\"OrphanAnimal\",\"name\":\"Toad\"\"}";
    assertThrows(InvalidTypeIdException.class, () -> objectMapper.readValue(jsonString, Animal.class));
  }
  
  @Test
  public void doesntRead_objectInWrongPackage() throws Exception {
    ObjectMapper objectMapper = 
        json("type").to(Animal.class, Person.class).mapper();
    
    String jsonString = "{\"type\":\"StrayCat\",\"price\":1,\"name\":\"Bella\",\"nickname\":\"Bee\"}";
    assertThrows(InvalidTypeIdException.class, () -> objectMapper.readValue(jsonString, Animal.class));

  }
}
