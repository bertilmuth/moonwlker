package org.requirementsascode.moonwlker;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * ObjectMapperBuilder supporting class for building Jackson's ObjectMapper instances (without types in JSON).
 * 
 * @author b_muth
 *
 */
public class UntypedJsonBuilder{
  private ObjectMapperBuilder objectMapperBuilder;

  public TypedJsonBuilder property(String typePropertyNane) {
    return ObjectMapperBuilder.typedJsonBuilder(typePropertyNane);
  }
  
  UntypedJsonBuilder(ObjectMapperBuilder objectMapperBuilder) {
    setObjectMapperBuilder(objectMapperBuilder);
  }
  
  /**
   * Creates a Jackson ObjectMapper based on the builder methods called so far.
   * 
   * @return the object mapper
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