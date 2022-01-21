package org.requirementsascode.moonwlker;

import org.requirementsascode.moonwlker.paramnames.AdaptedParameterNamesAnnotationIntrospector;
import org.requirementsascode.moonwlker.paramnames.ParameterExtractor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.requirementsascode.moonwlker.values.ValueTypeDeserializer;
import org.requirementsascode.moonwlker.values.ValueTypeSerializer;

import java.util.function.Function;

/**
 * Main entry point for the Moonwlker API. Register an instance of this class to configure
 * a Jackson's ObjectMapper instance.
 *
 * @author b_muth
 *
 */
@SuppressWarnings("serial")
public class MoonwlkerModule extends SimpleModule {
  /**
   *  The object mapper configurer
   */
  private ObjectMapperConfigurer objectMapperConfigurer;

  private MoonwlkerModule() {
    super("Moonwlker");
  }

  /**
   * Starts building a module for configuring Jackson's ObjectMapper instances.
   *
   * @return a builder.
   */
  public static MoonwlkerModuleBuilder builder() {
    MoonwlkerModule module = new MoonwlkerModule();
    MoonwlkerModuleBuilder moonwlkerModuleBuilder = module.new MoonwlkerModuleBuilder();
    return moonwlkerModuleBuilder;
  }

  @Override
  public void setupModule(SetupContext context) {
    super.setupModule(context);
    context.insertAnnotationIntrospector(new AdaptedParameterNamesAnnotationIntrospector(new ParameterExtractor()));

    ObjectMapper objectMapper = context.getOwner();
    objectMapperConfigurer().configure(objectMapper);
  }

  /**
   * Builder for a Moonwlker module.
   *
   * @author b_muth
   *
   */
  public class MoonwlkerModuleBuilder{

    private MoonwlkerModuleBuilder() {
      setObjectMapperConfigurer(new ObjectMapperConfigurer(this));
    }

    /**
     * Starts building a module for configuring Jackson's ObjectMapper instances.
     *
     * @param typePropertyName the name of property in JSON that contains the target class name.
     * @return a builder.
     */
    public PropertyMappingBuilder fromProperty(String typePropertyName) {
      if(typePropertyName == null) {
        throw new IllegalArgumentException("typePropertyName must not be null!");
      } else if(typePropertyName.length() == 0) {
        throw new IllegalArgumentException("typePropertyName must not be empty String!");
      }

      PropertyMappingBuilder builder = new PropertyMappingBuilder(objectMapperConfigurer(), typePropertyName);
      return builder;
    }

    /**
     * Creates a Moonwlker module based on the builder methods called so far.
     *
     * @return the module
     */
    public MoonwlkerModule build() {
      return MoonwlkerModule.this;
    }

    public <T> MoonwlkerModuleBuilder addValueType(Class<T> valueType, Function<T, String> valueToString, Function<String, T> stringToValue) {
      ValueTypeSerializer<T> serializer = new ValueTypeSerializer<>(valueType, valueToString);
      ValueTypeDeserializer<T> deserializer = new ValueTypeDeserializer<>(valueType, stringToValue);

      addSerializer(valueType, serializer);
      addDeserializer(valueType, deserializer);
      return this;
    }
  }

  /**
   * Returns the object mapper configurer for this module.
   *
   * @return the configurer
   */
  private ObjectMapperConfigurer objectMapperConfigurer() {
    return objectMapperConfigurer;
  }

  /**
   * Specifies the object mapper configurer for this module.
   *
   * @param objectMapperBuilder the builder
   */
  private void setObjectMapperConfigurer(ObjectMapperConfigurer objectMapperBuilder) {
    this.objectMapperConfigurer = objectMapperBuilder;
  }
}
