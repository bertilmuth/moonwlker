package org.requirementsascode.moonwlker;

import com.fasterxml.jackson.databind.ObjectMapper;

class UntypedJsonBuilder{
  private ObjectMapperBuilder objectMapperBuilder;

  UntypedJsonBuilder(ObjectMapperBuilder objectMapperBuilder) {
    setObjectMapperBuilder(objectMapperBuilder);
  }
  
  /**
   * Creates the ObjectMapper builder.
   * @return
   */
  public ObjectMapper mapper() {
    return objectMapperBuilder().mapper();
  }
  
  private ObjectMapperBuilder objectMapperBuilder() {
    return objectMapperBuilder;
  }

  private void setObjectMapperBuilder(ObjectMapperBuilder objectMapperBuilder) {
    this.objectMapperBuilder = objectMapperBuilder;
  }
}