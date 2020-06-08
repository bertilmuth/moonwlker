package org.requirementsascode.moonwlker;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.requirementsascode.moonwlker.testobject.animal.OrphanAnimal;
import static org.requirementsascode.moonwlker.Moonwlker.json;

import com.fasterxml.jackson.databind.ObjectMapper;

public class GeneralTest extends MoonwlkerTest {
  /*
   * Happy path tests 
   */
  
  @Test 
  public void readsAndWrites_oneObject() throws Exception {
    ObjectMapper objectMapper = 
        json().mapper();
    
    String jsonString = "{\"name\":\"Boo\"}";
    OrphanAnimal orphanAnimal = objectMapper.readValue(jsonString, OrphanAnimal.class);
    assertEquals("Boo", orphanAnimal.getName());
    assertEquals(jsonString, writeToJson(objectMapper, orphanAnimal));
  }

  @Test
  public void readsAndWritesObject_ignoresUnknownProperties() throws Exception {
    ObjectMapper objectMapper = 
        json().mapper();
    
    String jsonString = "{\"name\":\"Boo\",\"unknownProperty\":\"unknownValue\"}";
    OrphanAnimal orphanAnimal = objectMapper.readValue(jsonString, OrphanAnimal.class);
    assertEquals("Boo", orphanAnimal.getName());
    
    String expectedJsonString = "{\"name\":\"Boo\"}";
    assertEquals(expectedJsonString, writeToJson(objectMapper, orphanAnimal));
  }
}
