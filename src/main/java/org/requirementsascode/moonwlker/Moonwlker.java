package org.requirementsascode.moonwlker;

public class Moonwlker {

  public static ObjectMapperBuilder json(String typePropertyName) {
    return new ObjectMapperBuilder(typePropertyName);
  }
}
