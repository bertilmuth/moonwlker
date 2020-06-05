package org.requirementsascode.moonwlker;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

public abstract class MoonwlkerTest {

  public MoonwlkerTest() {
    super();
  }

  protected String writeToJson(ObjectMapper objectMapper, Object object) throws JsonProcessingException {
    ObjectWriter objectWriter = objectMapper.writer();
    String writtenJsonString = objectWriter.writeValueAsString(object);
    return writtenJsonString;
  }

}