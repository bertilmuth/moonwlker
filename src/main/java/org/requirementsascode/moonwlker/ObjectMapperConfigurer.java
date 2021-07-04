package org.requirementsascode.moonwlker;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;
import static com.fasterxml.jackson.annotation.PropertyAccessor.FIELD;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.requirementsascode.moonwlker.MoonwlkerModule.MoonwlkerModuleBuilder;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.impl.StdTypeResolverBuilder;

/**
 * Configures a Jackson ObjectMapper instance that can (de)serialize class
 * hierarchies.
 * 
 * @author b_muth
 *
 */
public class ObjectMapperConfigurer {
  private Collection<Class<?>> superClasses;
  private Map<Class<?>, String> superClassToPackagePrefixMap;
  private String typePropertyName;
  private MoonwlkerModuleBuilder moonwlkerModuleBuilder;
  
  ObjectMapperConfigurer(MoonwlkerModuleBuilder moonwlkerModuleBuilder) {
    setMoonwlkerModuleBuilder(moonwlkerModuleBuilder);
    clearSuperClasses();
    clearSuperClassToPackagePrefixMap();
  }

  /**
   * Configures a Jackson ObjectMapper based on the builder methods called so far.
   * 
   * @param objectMapper the ObjectMapper to configure
   */
  public void configure(ObjectMapper objectMapper) {
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
    objectMapper.setVisibility(FIELD, ANY);
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
  }
  
  /**
   * Creates a Moonwlker module based on the builder methods called so far.
   * 
   * @return the module
   */
  MoonwlkerModule build() {
    return moonwlkerModuleBuilder().build();
  }
  
  private MoonwlkerModuleBuilder moonwlkerModuleBuilder() {
    return moonwlkerModuleBuilder;
  }
  
  private void setMoonwlkerModuleBuilder(MoonwlkerModuleBuilder moonwlkerModuleBuilder) {
    this.moonwlkerModuleBuilder = moonwlkerModuleBuilder;
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
