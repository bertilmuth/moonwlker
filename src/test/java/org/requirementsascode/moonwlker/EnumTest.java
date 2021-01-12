package org.requirementsascode.moonwlker;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Test for issue 
 * https://github.com/bertilmuth/moonwlker/issues/2
 *
 */
class EnumTest {
  enum TestEnum {
    SHAKA, LAKA
  }

  @Test
  void serializeFirstEnumLiteral() throws JsonProcessingException {
    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(MoonwlkerModule.builder().build());

    TestEnum expectedEnum = TestEnum.SHAKA;
    String actualJsonString = mapper.writeValueAsString(expectedEnum);

    TestEnum actualEnum = mapper.readValue(actualJsonString, TestEnum.class);
    assertEquals(expectedEnum, actualEnum);
  }
  
  @Test
  void serializeSecondEnumLiteral() throws JsonProcessingException {
    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(MoonwlkerModule.builder().build());

    TestEnum expectedEnum = TestEnum.LAKA;
    String actualJsonString = mapper.writeValueAsString(expectedEnum);

    TestEnum actualEnum = mapper.readValue(actualJsonString, TestEnum.class);
    assertEquals(expectedEnum, actualEnum);
  }
}
