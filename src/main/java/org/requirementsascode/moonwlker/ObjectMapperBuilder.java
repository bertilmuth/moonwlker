package org.requirementsascode.moonwlker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.impl.StdTypeResolverBuilder;

class ObjectMapperBuilder {
  private ObjectMapper objectMapper;
  private Collection<Class<?>> superClasses;
  Map<Class<?>, String> superClassToPackagePrefixMap;
  
  /*
   * Builder properties
   */
  private boolean subclasses;
  private boolean ignoreUnknownProperties;
  private String propertyName = "type";

  public ObjectMapperBuilder() {
    clearSuperClasses();    
    clearSuperClassToPackagePrefixMap();    
    setObjectMapper(new ObjectMapper());
    ignoreUnknownProperties();
  }

  public ObjectMapper withSimpleName() {
    if(ignoreUnknownProperties) {
      objectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }
    
    if(subclasses) {
      PolymorphicTypeValidator ptv = SubClassValidator.forSubclassesOf(superClasses());
            
      StdTypeResolverBuilder typeResolverBuilder = new SubClassResolverBuilder(superClasses(), ptv)
        .init(Id.CUSTOM, new SubClassResolver(superClasses(), superClassToPackagePrefixMap()))
        .inclusion(As.PROPERTY)
        .typeIdVisibility(false)
        .typeProperty(propertyName);
      
      objectMapper().setDefaultTyping(typeResolverBuilder);
    }
    
    return objectMapper();
  }

  public SubclassesOf to(Class<?>... theSuperClasses) {
    List<Class<?>> superClasses = Arrays.asList(theSuperClasses);
    return new SubclassesOf(superClasses);
  }
  
  public ObjectMapperBuilder type(String propertyName) {
    this.propertyName = propertyName;
    return this;
  }
  
  private ObjectMapperBuilder ignoreUnknownProperties() {
    ignoreUnknownProperties = true;
    return this;
  }
  
  
  public class SubclassesOf{
    private List<Class<?>> superClasses;

    private SubclassesOf(List<Class<?>> superClasses) {
      this.superClasses = superClasses;
      subclasses = true;
      addSuperClasses(superClasses);
    }
    
    public ObjectMapperBuilder subClassesIn(String packageName) {
      mapEachClassToPackagePrefix(superClasses, superClassToPackagePrefixMap(), scl -> toPackagePrefix(packageName));
      return ObjectMapperBuilder.this;
    }

    public ObjectMapperBuilder subClasses() {
      mapEachClassToPackagePrefix(superClasses, superClassToPackagePrefixMap(), scl -> packagePrefixOf(scl));
      return ObjectMapperBuilder.this;
    }
  }
  
  private Map<Class<?>, String> mapEachClassToPackagePrefix(Collection<Class<?>> classesToBeMapped, Map<Class<?>, String> classToPackagePrefixMap, Function<Class<?>, String> classToPackagePrefixMapper) {
    for (Class<?> classToBeMapped : classesToBeMapped) {
      classToPackagePrefixMap.put(classToBeMapped, classToPackagePrefixMapper.apply(classToBeMapped));
    }
    return classToPackagePrefixMap;
  }
  
  private String toPackagePrefix(String packageName) {
    String packagePrefix = "".equals(packageName)? "" : packageName + ".";
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
    if(objectMapper == null) {
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
    if(superClasses == null) {
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
}
