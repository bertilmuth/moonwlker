package org.requirementsascode.moonwlker;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import com.fasterxml.jackson.databind.ObjectMapper;

class TypedJsonBuilder {
  private ObjectMapperBuilder objectMapperBuilder;

  TypedJsonBuilder(ObjectMapperBuilder objectMapperBuilder, String typePropertyName) {
    setObjectMapperBuilder(objectMapperBuilder);
    objectMapperBuilder().setTypePropertyName(typePropertyName);
  }

  public To to(Class<?>... theSuperClasses) {
    List<Class<?>> superClasses = Arrays.asList(theSuperClasses);
    return new To(superClasses);
  }

  private ObjectMapperBuilder objectMapperBuilder() {
    return objectMapperBuilder;
  }

  private void setObjectMapperBuilder(ObjectMapperBuilder objectMapperBuilder) {
    this.objectMapperBuilder = objectMapperBuilder;
  }

  public class To {
    private List<Class<?>> superClasses;

    private To(List<Class<?>> superClasses) {
      setSuperClasses(superClasses);
      objectMapperBuilder().addSuperClasses(superClasses());
    }

    public In in(String packageName) {
      return new In(packageName);
    }

    /**
     * Creates a Jackson ObjectMapper based on the builder methods called so far.
     * 
     * @return the object mapper
     */
    public ObjectMapper mapper() {
      mapEachSuperClassToOwnPackagePrefix();
      return objectMapperBuilder().mapper();
    }

    private void mapEachSuperClassToOwnPackagePrefix() {
      mapEachClassToPackagePrefix(superClasses(), objectMapperBuilder().superClassToPackagePrefixMap(),
          scl -> packagePrefixOf(scl));
    }

    private Map<Class<?>, String> mapEachClassToPackagePrefix(List<Class<?>> classesToBeMapped,
        Map<Class<?>, String> classToPackagePrefixMap, Function<Class<?>, String> classToPackagePrefixMapper) {
      for (Class<?> classToBeMapped : classesToBeMapped) {
        classToPackagePrefixMap.put(classToBeMapped, classToPackagePrefixMapper.apply(classToBeMapped));
      }
      return classToPackagePrefixMap;
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
    
    private List<Class<?>> superClasses() {
      return superClasses;
    }

    private void setSuperClasses(List<Class<?>> superClasses) {
      this.superClasses = superClasses;
    }

    public class In {
      private In(String packageName) {
        mapEachSuperClassToSpecifiedPackagePrefix(superClasses(), packageName);
      }

      public To to(Class<?>... superClasses) {
        return TypedJsonBuilder.this.to(superClasses);
      }

      /**
       * Creates a Jackson ObjectMapper based on the builder methods called so far.
       * 
       * @return the object mapper
       */
      public ObjectMapper mapper() {
        return objectMapperBuilder().mapper();
      }

      private void mapEachSuperClassToSpecifiedPackagePrefix(List<Class<?>> superClasses, String packageName) {
        mapEachClassToPackagePrefix(superClasses, objectMapperBuilder().superClassToPackagePrefixMap(),
            scl -> asPackagePrefix(packageName));
      }

      private String asPackagePrefix(String packageName) {
        String packagePrefix = "".equals(packageName) ? "" : packageName + ".";
        return packagePrefix;
      }
    }
  }
}