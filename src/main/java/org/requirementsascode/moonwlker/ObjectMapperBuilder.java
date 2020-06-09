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
  public static UntypedJson untypedJson() {
    ObjectMapperBuilder objectMapperBuilder = new ObjectMapperBuilder(null);
    UntypedJson untypedJson = objectMapperBuilder.new UntypedJson();
    return untypedJson;
  }

  /**
   * Create builder for classes in a hierarchy, so that the JSON needs to contain a type property.
   * 
   * @return the created builder
   */
  public static TypedJson typedJson(String typePropertyName) {
    ObjectMapperBuilder objectMapperBuilder = new ObjectMapperBuilder(typePropertyName);
    TypedJson typedJson = objectMapperBuilder.new TypedJson();
    return typedJson;
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
    objectMapper.setVisibility(FIELD, ANY);
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
  }
  
  public class UntypedJson{
    private UntypedJson() {
    }
    
    public ObjectMapper mapper() {
      return ObjectMapperBuilder.this.mapper();
    }
  }
  
  public class TypedJson{
    private TypedJson() {
    }
    
    public To to(Class<?>... theSuperClasses) {
      List<Class<?>> superClasses = Arrays.asList(theSuperClasses);
      return new To(superClasses);
    }
    
    public class To {
      private List<Class<?>> toSuperClasses;

      private To(List<Class<?>> toSuperClasses) {
        this.toSuperClasses = toSuperClasses;
        ObjectMapperBuilder.this.addSuperClasses(toSuperClasses);
      }

      public In in(String packageName) {
        return new In(packageName);
      }

      public ObjectMapper mapper() {
        mapEachSuperClassToItsOwnPackagePrefix(toSuperClasses);
        return ObjectMapperBuilder.this.mapper();
      }

      private void mapEachSuperClassToItsOwnPackagePrefix(List<Class<?>> superClasses) {
        mapEachClassToPackagePrefix(superClasses, superClassToPackagePrefixMap(), scl -> packagePrefixOf(scl));
      }
      
      public class In{
        private In(String packageName) {
          mapEachSuperClassToSpecifiedPackagePrefix(toSuperClasses, packageName);
        }

        private void mapEachSuperClassToSpecifiedPackagePrefix(List<Class<?>> superClasses, String packageName) {
          mapEachClassToPackagePrefix(superClasses, superClassToPackagePrefixMap(),
              scl -> asPackagePrefix(packageName));
        }

        public To to(Class<?>... theSuperClasses) { 
          return new TypedJson().to(theSuperClasses);
        }

        public ObjectMapper mapper() {
          return ObjectMapperBuilder.this.mapper();
        }
      }
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
