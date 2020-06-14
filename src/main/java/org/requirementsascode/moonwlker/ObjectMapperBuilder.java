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
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.impl.StdTypeResolverBuilder;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;

/**
 * A builder for Jackson ObjectMapper instances that can (de)serialize class
 * hierarchies.
 * 
 * @author b_muth
 *
 */
public class ObjectMapperBuilder {
  private Collection<Class<?>> superClasses;
  private Map<Class<?>, String> superClassToPackagePrefixMap;
  private String typePropertyName;
  private Module module;
  
  ObjectMapperBuilder(Module module) {
    this.module = module;
    clearSuperClasses();
    clearSuperClassToPackagePrefixMap();
  }

  public MoonwlkerModuleBuilder builder() {
    MoonwlkerModuleBuilder json = new MoonwlkerModuleBuilder(this);
    return json;
  } 

  /**
   * Configures a Jackson ObjectMapper based on the builder methods called so far.
   * 
   * @param objectMapper the ObjectMapper to configure
   */
  public void buildOn(ObjectMapper objectMapper) {
    activateDefaultSettingsFor(objectMapper);    
    
    if (superClasses() != null) {
      PolymorphicTypeValidator ptv = SubClassValidator.forSubclassesOf(superClasses());

      StdTypeResolverBuilder typeResolverBuilder = 
        new SubClassResolverBuilder(superClasses(), ptv)
          .init(Id.CUSTOM, new SubClassResolver(superClasses(), superClassToPackagePrefixMap()))
          .inclusion(As.PROPERTY)
          .typeIdVisibility(false)
          .typeProperty(typePropertyName());

      objectMapper.setDefaultTyping(typeResolverBuilder);
    }
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
    objectMapper.registerModule(new Jdk8Module());
    objectMapper.setVisibility(FIELD, ANY);
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
  }
  
  Module build() {
    return module;
  }
  
  private String typePropertyName() {
    return typePropertyName;
  }

  void setTypePropertyName(String typePropertyName) {
    this.typePropertyName = typePropertyName;
  }
  
  private void clearSuperClassToPackagePrefixMap() {
    superClassToPackagePrefixMap = new HashMap<>();
  }

  Map<Class<?>, String> superClassToPackagePrefixMap() {
    return superClassToPackagePrefixMap;
  }

  private Collection<Class<?>> superClasses() {
    return superClasses;
  }

  private void clearSuperClasses() {
    this.superClasses = new ArrayList<>();
  }

  void addSuperClasses(Collection<Class<?>> superClasses) {
    this.superClasses.addAll(superClasses);
  }
}
