package org.requirementsascode.moonwlker;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.requirementsascode.moonwlker.Moonwlker;
import org.requirementsascode.moonwlker.testobject.person.Person;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;

public class GeneralTest extends MoonwlkerTest {
  /*
   * Happy path tests
   */
  
  @Test
  public void readsAndWritesObject_oneObject() throws Exception {
    ObjectMapper objectMapper = 
        Moonwlker.objectMapperBuilder()
          .build();
    
    String jsonString = "{\"firstName\":\"Jane\",\"lastName\":\"Doe\"}";
    Person person = objectMapper.readValue(jsonString, Person.class);
    assertEquals("Jane", person.getFirstName());
    assertEquals("Doe", person.getLastName());
    
    String expectedWrittenJson = "{\"firstName\":\"Jane\",\"lastName\":\"Doe\"}";
    assertEquals(expectedWrittenJson, writeToJson(objectMapper, person));
  }
  
  @Test
  public void readsAndWritesObject_ignoreUnknownProperties() throws Exception {
    ObjectMapper objectMapper = 
        Moonwlker.objectMapperBuilder()
          .ignoreUnknownProperties()
          .build();
    
    String jsonString = "{\"firstName\":\"Jane\",\"lastName\":\"Doe\",\"unknownProperty\":\"unknownValue\"}";
    Person person = objectMapper.readValue(jsonString, Person.class);
    assertEquals("Jane", person.getFirstName());
    assertEquals("Doe", person.getLastName());
    
    String expectedWrittenJson = "{\"firstName\":\"Jane\",\"lastName\":\"Doe\"}";
    assertEquals(expectedWrittenJson, writeToJson(objectMapper, person));
  }
  
  /*
   * Error path tests
   */
  
  @Test(expected = UnrecognizedPropertyException.class)
  public void doesntReadObjectWithUnknownProperty() throws Exception {
    ObjectMapper objectMapper = 
        Moonwlker.objectMapperBuilder()
          .build();
    
    String jsonString = "{\"firstName\":\"Jane\",\"lastName\":\"Doe\",\"unknownProperty\":\"unknownValue\"}";
    objectMapper.readValue(jsonString, Person.class);
  }
}
