package org.requirementsascode.moonwlker;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.requirementsascode.moonwlker.testobject.animal.Cat;
import org.requirementsascode.moonwlker.testobject.animal.Lives;

import com.fasterxml.jackson.databind.ObjectMapper;

public class NumericValueTest extends MoonwlkerModuleTest {

  @Test
  public void reads_oneValue() throws Exception {
    ObjectMapper objectMapper = getObjectMapper();

    String jsonString = "9";
    Lives lives = objectMapper.readValue(jsonString, Lives.class);
    assertEquals(9, lives.value());
  }

  @Test
  public void writes_oneJsonValue() throws Exception {
    ObjectMapper objectMapper = getObjectMapper();

    Lives lives = new Lives(9);
    assertEquals("9", writeToJson(objectMapper, lives));
  }

  @Test
  public void reads_oneObjectWithJsonValue() throws Exception {
    ObjectMapper objectMapper = getObjectMapper();


    String jsonString = "{\"price\":1.10,\"name\":\"Prince Herbert\",\"nickname\":\"Herbie\",\"lives\":9}";
    Cat cat = objectMapper.readValue(jsonString, Cat.class);
    assertEquals(9, cat.lives().value());
  }

  @Test
  public void writes_oneObjectWithJsonValue() throws Exception {
    ObjectMapper objectMapper = getObjectMapper();

    Cat cat = new Cat(BigDecimal.valueOf(110, 2), "Prince Herbert", "Herbie", new Lives(9));
    assertEquals("{\"price\":1.10,\"name\":\"Prince Herbert\",\"nickname\":\"Herbie\",\"lives\":9}", writeToJson(objectMapper, cat));
  }

  private ObjectMapper getObjectMapper() {

    MoonwlkerModule module = MoonwlkerModule.builder()
      .addNumericValueType(Lives.class, Lives::value, Lives::new)
      .build();

    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(module);
    return objectMapper;
  }

}
