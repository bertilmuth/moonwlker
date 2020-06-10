package org.requirementsascode.moonwlker;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;
import static com.fasterxml.jackson.annotation.PropertyAccessor.FIELD;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonCreator.Mode;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.impl.StdTypeResolverBuilder;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;

/**
 * A builder for Jackson ObjectMapper instances that can (de)serialize class
 * hierarchies.
 * 
 * @author b_muth
 *
 */
public class ObjectMapperBuilder {
  private ObjectMapper objectMapper;
  private Collection<Class<?>> superClasses;
  Map<Class<?>, String> superClassToPackagePrefixMap;

  /*
   * Builder properties
   */
  private String typePropertyName;

  /**
   * Create builder for specific classes, so that the JSON doesn't need to contain a type property.
   * 
   * @return the created builder
   */
  public static UntypedJson untypedJson() {
    ObjectMapperBuilder objectMapperBuilder = new ObjectMapperBuilder();
    UntypedJson untypedJson = objectMapperBuilder.new UntypedJson();
    return untypedJson;
  }

  /**
   * Create builder for classes in a hierarchy, so that the JSON needs to contain a type property.
   * 
   * @return the created builder
   */
  public static TypedJson typedJson(String typePropertyName) {
    ObjectMapperBuilder objectMapperBuilder = new ObjectMapperBuilder();
    TypedJson typedJson = new TypedJson(objectMapperBuilder, typePropertyName);
    return typedJson;
  }
  
  private ObjectMapperBuilder() {
    clearSuperClasses();
    clearSuperClassToPackagePrefixMap();
    setObjectMapper(new ObjectMapper());
    activateDefaultSettingsFor(objectMapper());    
  }

  private void activateDefaultSettingsFor(ObjectMapper objectMapper) {
    objectMapper.setAnnotationIntrospector(new JacksonAnnotationIntrospector() {
      private static final long serialVersionUID = 1L;
      @Override
      public Mode findCreatorAnnotation(MapperConfig<?> config, Annotated a) {
        return JsonCreator.Mode.PROPERTIES;
      }
    });
    objectMapper.registerModule(new ParameterNamesModule());
    objectMapper.setVisibility(FIELD, ANY);
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
  }
  
  public class UntypedJson{
    private UntypedJson() {
    }
    
    public ObjectMapper mapper() {
      return objectMapperBuilder().mapper();
    }
    
    private ObjectMapperBuilder objectMapperBuilder() {
      return ObjectMapperBuilder.this;
    }
  }

  /**
   * Creates a Jackson ObjectMapper based on the builder methods called so far.
   * 
   * @return the object mapper
   */
  public ObjectMapper mapper() {
    if (typePropertyName != null) {
      PolymorphicTypeValidator ptv = SubClassValidator.forSubclassesOf(superClasses());

      StdTypeResolverBuilder typeResolverBuilder = 
        new SubClassResolverBuilder(superClasses(), ptv)
          .init(Id.CUSTOM, new SubClassResolver(superClasses(), superClassToPackagePrefixMap()))
          .inclusion(As.PROPERTY)
          .typeIdVisibility(false)
          .typeProperty(typePropertyName());

      objectMapper().setDefaultTyping(typeResolverBuilder);
    }

    return objectMapper();
  }

  private ObjectMapper objectMapper() {
    return objectMapper;
  }

  private void setObjectMapper(ObjectMapper objectMapper) {
    if (objectMapper == null) {
      throw new IllegalArgumentException("objectMapper is null, but must be non-null");
    }
    this.objectMapper = objectMapper;
  }

  private Collection<Class<?>> superClasses() {
    return superClasses;
  }

  private void clearSuperClasses() {
    this.superClasses = new ArrayList<>();
  }

  void addSuperClasses(Collection<Class<?>> superClasses) {
    if (superClasses == null) {
      throw new IllegalArgumentException("superClasses is null, but must be non-null");
    }
    this.superClasses.addAll(superClasses);
  }

  private void clearSuperClassToPackagePrefixMap() {
    superClassToPackagePrefixMap = new HashMap<>();
  }

  Map<Class<?>, String> superClassToPackagePrefixMap() {
    return superClassToPackagePrefixMap;
  }

  private String typePropertyName() {
    return typePropertyName;
  }

  void setTypePropertyName(String typePropertyName) {
    this.typePropertyName = typePropertyName;
  }
}
