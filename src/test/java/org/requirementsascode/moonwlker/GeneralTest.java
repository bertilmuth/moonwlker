package org.requirementsascode.moonwlker;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.requirementsascode.moonwlker.Moonwlker.json;

import org.junit.jupiter.api.Test;
import org.requirementsascode.moonwlker.testobject.person.Person;

import com.fasterxml.jackson.databind.ObjectMapper;

public class GeneralTest extends MoonwlkerTest {
  /*
   * Happy path tests 
   */
  
  @Test
  public void readsAndWritesObject_oneObject() throws Exception {
    ObjectMapper objectMapper = 
        json("type").mapper();
    
    String jsonString = "{\"firstName\":\"Jane\",\"lastName\":\"Doe\"}";
    Person person = objectMapper.readValue(jsonString, Person.class);
    assertEquals("Jane", person.getFirstName());
    assertEquals("Doe", person.getLastName());
    
    String expectedWrittenJson = "{\"firstName\":\"Jane\",\"lastName\":\"Doe\"}";
    assertEquals(expectedWrittenJson, writeToJson(objectMapper, person));
  }
  
  @Test
  public void readsAndWritesObject_ignoresUnknownProperties() throws Exception {
    ObjectMapper objectMapper = 
        json("type").mapper();
    
    String jsonString = "{\"firstName\":\"Jane\",\"lastName\":\"Doe\",\"unknownProperty\":\"unknownValue\"}";
    Person person = objectMapper.readValue(jsonString, Person.class);
    assertEquals("Jane", person.getFirstName());
    assertEquals("Doe", person.getLastName());
    
    String expectedWrittenJson = "{\"firstName\":\"Jane\",\"lastName\":\"Doe\"}";
    assertEquals(expectedWrittenJson, writeToJson(objectMapper, person));
  }
}
