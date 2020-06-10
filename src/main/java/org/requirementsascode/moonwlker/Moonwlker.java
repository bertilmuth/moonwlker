package org.requirementsascode.moonwlker;

public class Moonwlker {
  public static UntypedJsonBuilder json() {
    return ObjectMapperBuilder.untypedJsonBuilder();
  }

  public static TypedJsonBuilder json(String typePropertyName) {
    return ObjectMapperBuilder.typedJsonBuilder(typePropertyName);
  }
}
