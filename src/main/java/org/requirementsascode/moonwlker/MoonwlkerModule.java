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
  }

  /**
   * Starts building Jackson's ObjectMapper instances.
   * 
   * @return a builder.
   */
  public static MoonwlkerModuleBuilder builder() {
    MoonwlkerModule moonwlker = new MoonwlkerModule();
    ObjectMapperBuilder objectMapperBuilder = new ObjectMapperBuilder(moonwlker);
    moonwlker.setObjectMapperBuilder(objectMapperBuilder);
    return objectMapperBuilder.json();
  }

  @Override
  public void setupModule(SetupContext context) {
    super.setupModule(context);
    ObjectMapper objectMapper = context.getOwner();
    objectMapperBuilder.buildOn(objectMapper);
  }

  private void setObjectMapperBuilder(ObjectMapperBuilder objectMapperBuilder) {
    this.objectMapperBuilder = objectMapperBuilder;
  }
}
