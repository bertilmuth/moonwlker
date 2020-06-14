package org.requirementsascode.moonwlker;

/**
 * ObjectMapperBuilder supporting class for building Jackson's ObjectMapper instances.
 * 
 * @author b_muth
 *
 */
public class MoonwlkerModuleBuilder{
  private ObjectMapperBuilder objectMapperBuilder;
  
  MoonwlkerModuleBuilder(ObjectMapperBuilder objectMapperBuilder) {
    setObjectMapperBuilder(objectMapperBuilder);
  }

  /**
   * Starts building a module for configuring Jackson's ObjectMapper instances.
   * 
   * @param typePropertyName the name of property in JSON that contains the target class name.
   * @return a builder.
   */
  public Property fromProperty(String typePropertyName) {
    if(typePropertyName == null) {
      throw new IllegalArgumentException("typePropertyName must not be null!");
    } else if(typePropertyName.length() == 0) {
      throw new IllegalArgumentException("typePropertyName must not be empty String!");
    }
    
    Property property = new Property(objectMapperBuilder(), typePropertyName);
    return property;
  }
  
  /**
   * Creates a Moonwlker module based on the builder methods called so far.
   * 
   * @return the module
   */
  public MoonwlkerModule build() {
    return objectMapperBuilder().build();
  }
  
  ObjectMapperBuilder objectMapperBuilder() {
    return objectMapperBuilder;
  }

  private void setObjectMapperBuilder(ObjectMapperBuilder objectMapperBuilder) {
    this.objectMapperBuilder = objectMapperBuilder;
  }
}