package org.requirementsascode.moonwlker;

import com.fasterxml.jackson.databind.ObjectMapper;

class UntypedJsonBuilder{
  private final ObjectMapperBuilder objectMapperBuilder;

  UntypedJsonBuilder(ObjectMapperBuilder objectMapperBuilder) {
    this.objectMapperBuilder = objectMapperBuilder;
  }
  
  public ObjectMapper mapper() {
    return objectMapperBuilder().mapper();
  }
  
  private ObjectMapperBuilder objectMapperBuilder() {
    return objectMapperBuilder;
  }
}