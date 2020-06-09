package org.requirementsascode.moonwlker;

public class Moonwlker {
  public static ObjectMapperBuilder json() {
    return ObjectMapperBuilder.untypedJson();
  }

  public static ObjectMapperBuilder json(String typePropertyName) {
    return ObjectMapperBuilder.typedJson(typePropertyName);
  }
}
