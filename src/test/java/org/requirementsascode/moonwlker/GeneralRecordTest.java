package org.requirementsascode.moonwlker;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.requirementsascode.moonwlker.testobject.animal.EmptyObject_Record;
import org.requirementsascode.moonwlker.testobject.animal.OrphanAnimal_Record;

import com.fasterxml.jackson.databind.ObjectMapper;

public class GeneralRecordTest extends MoonwlkerModuleTest {
  /*
   * Happy path tests
   */
  
  @Test
  public void readsAndWrites_emptyObject() throws Exception {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(MoonwlkerModule.builder().build());

    String jsonString = "{}";
    EmptyObject_Record emptyObject = objectMapper.readValue(jsonString, EmptyObject_Record.class);
    assertEquals(jsonString, writeToJson(objectMapper, emptyObject));
  }

  @Test
  public void readsAndWrites_oneObject() throws Exception {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(MoonwlkerModule.builder().build());

    String jsonString = "{\"name\":\"Boo\"}";
    OrphanAnimal_Record orphanAnimal = objectMapper.readValue(jsonString, OrphanAnimal_Record.class);
    assertEquals("Boo", orphanAnimal.name());
    assertEquals(jsonString, writeToJson(objectMapper, orphanAnimal));
  }

  @Test
  public void readsAndWritesObject_ignoresUnknownProperties() throws Exception {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(MoonwlkerModule.builder().build());

    String jsonString = "{\"name\":\"Boo\",\"unknownProperty\":\"unknownValue\"}";
    OrphanAnimal_Record orphanAnimal = objectMapper.readValue(jsonString, OrphanAnimal_Record.class);
    assertEquals("Boo", orphanAnimal.name());

    String expectedJsonString = "{\"name\":\"Boo\"}";
    assertEquals(expectedJsonString, writeToJson(objectMapper, orphanAnimal));
  }
}
