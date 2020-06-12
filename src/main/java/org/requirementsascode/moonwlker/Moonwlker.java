package org.requirementsascode.moonwlker;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

/**
 * Main entry point for the Moonwlker API. Use this class to create builders for
 * Jackson's ObjectMapper instances.
 * 
 * @author b_muth
 *
 */
public class Moonwlker extends SimpleModule {
  private static final long serialVersionUID = 1L;
  private ObjectMapperBuilder objectMapperBuilder;
  
  private Moonwlker() {
    super("Moonwlker");
  }

  /**
   * Starts building Jackson's ObjectMapper instances.
   * 
   * @return a builder.
   */
  public static Json map() {
    Moonwlker moonwlker = new Moonwlker();
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
