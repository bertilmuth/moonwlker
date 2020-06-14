package org.requirementsascode.moonwlker;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.requirementsascode.moonwlker.MoonwlkerModule;

import org.junit.jupiter.api.Test;
import org.requirementsascode.moonwlker.testobject.animal.Dog;
import org.requirementsascode.moonwlker.testobject.animal.OrphanAnimal;

import com.fasterxml.jackson.databind.ObjectMapper;

public class GeneralTest extends MoonwlkerModuleTest {
  /*
   * Happy path tests
   */

  @Test
  public void readsAndWrites_oneObject() throws Exception {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(MoonwlkerModule.builder().build());

    String jsonString = "{\"name\":\"Boo\"}";
    OrphanAnimal orphanAnimal = objectMapper.readValue(jsonString, OrphanAnimal.class);
    assertEquals("Boo", orphanAnimal.getName());
    assertEquals(jsonString, writeToJson(objectMapper, orphanAnimal));
  }

  @Test
  public void readsAndWrites_oneObject_WithSuperclass() throws Exception {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(MoonwlkerModule.builder().build());

    String jsonString = "{\"price\":412,\"name\":\"Calla\",\"command\":\"Sit\"}";
    Dog dog = objectMapper.readValue(jsonString, Dog.class);
    assertEquals("Calla", dog.name());
    assertEquals(jsonString, writeToJson(objectMapper, dog));
  }

  @Test
  public void readsAndWritesObject_ignoresUnknownProperties() throws Exception {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(MoonwlkerModule.builder().build());

    String jsonString = "{\"name\":\"Boo\",\"unknownProperty\":\"unknownValue\"}";
    OrphanAnimal orphanAnimal = objectMapper.readValue(jsonString, OrphanAnimal.class);
    assertEquals("Boo", orphanAnimal.getName());

    String expectedJsonString = "{\"name\":\"Boo\"}";
    assertEquals(expectedJsonString, writeToJson(objectMapper, orphanAnimal));
  }
}
