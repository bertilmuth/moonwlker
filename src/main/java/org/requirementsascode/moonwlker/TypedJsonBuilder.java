package org.requirementsascode.moonwlker;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import com.fasterxml.jackson.databind.ObjectMapper;

public class TypedJsonBuilder {
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
    private List<Class<?>> toSuperClasses;

    private To(List<Class<?>> toSuperClasses) {
      this.toSuperClasses = toSuperClasses;
      objectMapperBuilder().addSuperClasses(toSuperClasses);
    }

    public In in(String packageName) {
      return new In(packageName);
    }

    public ObjectMapper mapper() {
      mapEachSuperClassToItsOwnPackagePrefix(toSuperClasses);
      return objectMapperBuilder().mapper();
    }

    private void mapEachSuperClassToItsOwnPackagePrefix(List<Class<?>> superClasses) {
      mapEachClassToPackagePrefix(superClasses, objectMapperBuilder().superClassToPackagePrefixMap(),
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

    public class In {
      private In(String packageName) {
        mapEachSuperClassToSpecifiedPackagePrefix(toSuperClasses, packageName);
      }

      public To to(Class<?>... theSuperClasses) {
        return TypedJsonBuilder.this.to(theSuperClasses);
      }

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