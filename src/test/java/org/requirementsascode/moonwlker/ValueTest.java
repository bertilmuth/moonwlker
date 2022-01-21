package org.requirementsascode.moonwlker;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.requirementsascode.moonwlker.testobject.animal.ObjectWithJsonValue;
import org.requirementsascode.moonwlker.testobject.animal.OrphanAnimal;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ValueTest extends MoonwlkerModuleTest {

  @Test
  public void reads_oneValue() throws Exception {
    ObjectMapper objectMapper = getObjectMapper();

    String jsonString = "\"Boo\"";
    OrphanAnimal orphanAnimal = objectMapper.readValue(jsonString, OrphanAnimal.class);
    assertEquals("Boo", orphanAnimal.getName());
  }

  @Test
  public void writes_oneJsonValue() throws Exception {
    ObjectMapper objectMapper = getObjectMapper();

    OrphanAnimal orphanAnimal = new OrphanAnimal("Boo");
    assertEquals("\"Boo\"", writeToJson(objectMapper, orphanAnimal));
  }

  @Test
  public void reads_oneObjectWithJsonValue() throws Exception {
    ObjectMapper objectMapper = getObjectMapper();


    String jsonString = "{\"someString\":\"blah\",\"orphanAnimal\":\"Boo\"}";
    ObjectWithJsonValue object = objectMapper.readValue(jsonString, ObjectWithJsonValue.class);
    assertEquals("Boo", object.getOrphanAnimal().getName());
  }

  @Test
  public void writes_oneObjectWithJsonValue() throws Exception {
    ObjectMapper objectMapper = getObjectMapper();

    OrphanAnimal orphanAnimal = new OrphanAnimal("Boo");
   ObjectWithJsonValue object = new ObjectWithJsonValue("blah", orphanAnimal);
    assertEquals("{\"someString\":\"blah\",\"orphanAnimal\":\"Boo\"}", writeToJson(objectMapper, object));
  }

  private ObjectMapper getObjectMapper() {

    MoonwlkerModule module = MoonwlkerModule.builder()
            .addValueType(OrphanAnimal.class, OrphanAnimal::getName, OrphanAnimal::new)
            .build();

    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(module);
    return objectMapper;
  }

}
