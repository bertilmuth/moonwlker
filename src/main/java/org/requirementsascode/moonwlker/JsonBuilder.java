package org.requirementsascode.moonwlker;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * ObjectMapperBuilder supporting class for building Jackson's ObjectMapper instances.
 * 
 * @author b_muth
 *
 */
public class JsonBuilder{
  private ObjectMapperBuilder objectMapperBuilder;

  /**
   * Starts building Jackson's ObjectMapper instances (with types in JSON).
   * 
   * @param typePropertyName the name of property in JSON that contains the target class name.
   * @return a builder
   */
  public TypedJsonBuilder property(String typePropertyName) {
    if(typePropertyName == null) {
      throw new IllegalArgumentException("typePropertyName must not be null!");
    } else if(typePropertyName.length() == 0) {
      throw new IllegalArgumentException("typePropertyName must not be empty String!");
    }
    
    return ObjectMapperBuilder.typedJsonBuilder(typePropertyName);
  }
  
  JsonBuilder(ObjectMapperBuilder objectMapperBuilder) {
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