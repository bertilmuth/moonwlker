package org.requirementsascode.moonwlker;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

/**
 * Main entry point for the Moonwlker API. Register an instance of this class to configure
 * a Jackson's ObjectMapper instance.
 * 
 * @author b_muth
 *
 */
public class MoonwlkerModule extends SimpleModule {
  private static final long serialVersionUID = 1L;
  private ObjectMapperBuilder objectMapperBuilder;
  
  private MoonwlkerModule() {
    super("Moonwlker");
    setObjectMapperBuilder(new ObjectMapperBuilder(this));
  }

  /**
   * Starts building a module for configuring Jackson's ObjectMapper instances.
   * 
   * @return a builder.
   */
  public static MoonwlkerModule builder() {
    MoonwlkerModule moonwlkerModule = new MoonwlkerModule();
    return moonwlkerModule;
  }

  @Override
  public void setupModule(SetupContext context) {
    super.setupModule(context);
    ObjectMapper objectMapper = context.getOwner();
    objectMapperBuilder().buildOn(objectMapper);
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
