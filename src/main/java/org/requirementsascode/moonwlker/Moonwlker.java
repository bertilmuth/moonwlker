package org.requirementsascode.moonwlker;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Main entry point for the Moonwlker API.
 * Use this class to create builders for Jackson's ObjectMapper instances.
 * 
 * @author b_muth
 *
 */
public class Moonwlker {
  /**
   * Starts building Jackson's ObjectMapper instances.
   * 
   * @return a builder.
   */
  public static Json json() {
    ObjectMapper objectMapper = new ObjectMapper();
    return ObjectMapperBuilder.jsonFor(objectMapper);
  }
}
