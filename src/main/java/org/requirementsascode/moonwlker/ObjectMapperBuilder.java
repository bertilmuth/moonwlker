package org.requirementsascode.moonwlker;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;
import static com.fasterxml.jackson.annotation.PropertyAccessor.FIELD;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

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
  public static ObjectMapperBuilder untypedJson() {
    return new ObjectMapperBuilder(null);
  }

  /**
   * Create builder for classes in a hierarchy, so that the JSON needs to contain a type property.
   * 
   * @return the created builder
   */
  public static ObjectMapperBuilder typedJson(String typePropertyName) {
    return new ObjectMapperBuilder(typePropertyName);
  }
  
  private ObjectMapperBuilder(String typePropertyName) {
    setTypePropertyName(typePropertyName);
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
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    objectMapper.setVisibility(FIELD, ANY);
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

  public SubclassesOf to(Class<?>... theSuperClasses) {
    List<Class<?>> superClasses = Arrays.asList(theSuperClasses);
    return new SubclassesOf(superClasses);
  }

  public class SubclassesOf {
    private List<Class<?>> localSuperClasses;

    private SubclassesOf(List<Class<?>> superClasses) {
      localSuperClasses = superClasses;
      addSuperClasses(superClasses);
    }

    public ObjectMapperBuilder in(String packageName) {
      mapEachClassToPackagePrefix(localSuperClasses, superClassToPackagePrefixMap(),
          scl -> asPackagePrefix(packageName));
      return ObjectMapperBuilder.this;
    }

    public ObjectMapper mapper() {
      mapEachClassToPackagePrefix(localSuperClasses, superClassToPackagePrefixMap(), scl -> packagePrefixOf(scl));
      return ObjectMapperBuilder.this.mapper();
    }
  }

  private Map<Class<?>, String> mapEachClassToPackagePrefix(List<Class<?>> classesToBeMapped,
      Map<Class<?>, String> classToPackagePrefixMap, Function<Class<?>, String> classToPackagePrefixMapper) {
    for (Class<?> classToBeMapped : classesToBeMapped) {
      classToPackagePrefixMap.put(classToBeMapped, classToPackagePrefixMapper.apply(classToBeMapped));
    }
    return classToPackagePrefixMap;
  }

  private String asPackagePrefix(String packageName) {
    String packagePrefix = "".equals(packageName) ? "" : packageName + ".";
    return packagePrefix;
  }

  private String packagePrefixOf(Class<?> aClass) {
    String className = aClass.getName();
    int ix = className.lastIndexOf('.');
    String packagePrefix;
    if (ix < 0) { // can this ever occur?
      packagePrefix = ".";
    } else {
      packagePrefix = className.substring(0, ix + 1);
    }
    return packagePrefix;
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

  private void addSuperClasses(Collection<Class<?>> superClasses) {
    if (superClasses == null) {
      throw new IllegalArgumentException("superClasses is null, but must be non-null");
    }
    this.superClasses.addAll(superClasses);
  }

  private void clearSuperClassToPackagePrefixMap() {
    superClassToPackagePrefixMap = new HashMap<>();
  }

  private Map<Class<?>, String> superClassToPackagePrefixMap() {
    return superClassToPackagePrefixMap;
  }

  private String typePropertyName() {
    return typePropertyName;
  }

  private void setTypePropertyName(String typePropertyName) {
    this.typePropertyName = typePropertyName;
  }
}
