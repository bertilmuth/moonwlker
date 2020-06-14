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
  private MoonwlkerModuleBuilder moonwlkerModuleBuilder;
  
  private MoonwlkerModule() {
    super("Moonwlker");
    ObjectMapperBuilder objectMapperBuilder = new ObjectMapperBuilder(this);
    setMoonwlkerModuleBuilder(new MoonwlkerModuleBuilder(objectMapperBuilder));
  }

  /**
   * Starts building Jackson's ObjectMapper instances.
   * 
   * @return a builder.
   */
  public static MoonwlkerModuleBuilder builder() {
    MoonwlkerModule moonwlkerModule = new MoonwlkerModule();
    return moonwlkerModule.moonwlkerModuleBuilder();
  }

  @Override
  public void setupModule(SetupContext context) {
    super.setupModule(context);
    ObjectMapper objectMapper = context.getOwner();
    moonwlkerModuleBuilder().objectMapperBuilder().buildOn(objectMapper);
  }

  private MoonwlkerModuleBuilder moonwlkerModuleBuilder() {
    return moonwlkerModuleBuilder;
  }
  
  private void setMoonwlkerModuleBuilder(MoonwlkerModuleBuilder moonwlkerModuleBuilder) {
    this.moonwlkerModuleBuilder = moonwlkerModuleBuilder;
  }
}
