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

  /**
   * Specifies the super classes whose sub classes will be (de)serialized.
   * 
   * @param theSuperClasses super classes that are roots of (de)serialization.
   * @return another builder 
   */
  public ToBuilder to(Class<?>... theSuperClasses) {
    if(theSuperClasses == null) {
      throw new IllegalArgumentException("theSuperClasses must not be null!");
    }
    
    List<Class<?>> superClasses = Arrays.asList(theSuperClasses);
    return new ToBuilder(superClasses);
  }

  private ObjectMapperBuilder objectMapperBuilder() {
    return objectMapperBuilder;
  }

  private void setObjectMapperBuilder(ObjectMapperBuilder objectMapperBuilder) {
    this.objectMapperBuilder = objectMapperBuilder;
  }

  public class ToBuilder {
    private List<Class<?>> superClasses;

    private ToBuilder(List<Class<?>> superClasses) {
      setSuperClasses(superClasses);
      objectMapperBuilder().addSuperClasses(superClasses());
    }

    /**
     * Specifies the name of the package that is scanned for subclasses.
     * 
     * @param packageName the full name of the package, no dot at the end.
     * @return another builder 
     */
    public InBuilder in(String packageName) {
      if(packageName == null) {
        throw new IllegalArgumentException("packageName must not be null!");
      }
      
      return new InBuilder(packageName);
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

    public class InBuilder {
      private InBuilder(String packageName) {
        mapEachSuperClassToSpecifiedPackagePrefix(superClasses(), packageName);
      }

      public ToBuilder to(Class<?>... superClasses) {
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