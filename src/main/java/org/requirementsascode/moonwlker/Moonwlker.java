package org.requirementsascode.moonwlker;

/**
 * Main entry point for the Moonwlker API.
 * Use this class to create builders for Jackson's ObjectMapper instances.
 * 
 * @author b_muth
 *
 */
public class Moonwlker {
  /**
   * Starts building Jackson's ObjectMapper instances (without types in JSON).
   * 
   * @return a builder
   */
  public static UntypedJsonBuilder json() {
    return ObjectMapperBuilder.untypedJsonBuilder();
  }

  /**
   * Starts building Jackson's ObjectMapper instances (with types in JSON).
   * 
   * @return a builder
   */
  public static TypedJsonBuilder json(String typePropertyName) {
    return ObjectMapperBuilder.typedJsonBuilder(typePropertyName);
  }
}
