package org.requirementsascode.moonwlker;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.databind.DatabindContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.jsontype.impl.TypeIdResolverBase;
import com.fasterxml.jackson.databind.type.TypeFactory;

class SubClassResolver extends TypeIdResolverBase {
  private Collection<Class<?>> superClasses;
  private Map<Class<?>, String> superClassToPackagePrefixMap;

  public SubClassResolver(Collection<Class<?>> superClasses, Map<Class<?>, String> superClassToPackagePrefixMap) {
    super(TypeFactory.defaultInstance().constructType(Object.class), TypeFactory.defaultInstance());
    setSuperClassToPackagePrefixMap(superClassToPackagePrefixMap);
    setSuperClasses(superClasses);
  }

  @Override
  public String idFromValue(Object currentObject) {
    return idFromValueAndType(currentObject, currentObject.getClass());
  }
  
  @Override
  public String idFromValueAndType(Object currentObject, Class<?> suggestedType) {
    String id = suggestedType.getSimpleName();
    return id;
  }

  @Override
  public JavaType typeFromId(DatabindContext context, String simpleClassName) throws IOException {
    JavaType subType = null;

    for (Class<?> superClass : superClasses()) {
      final String packagePrefix = packagePrefixFromMap(superClass);
      JavaType optionalTypeInPackage = optionalTypeInPackageWith(packagePrefix, simpleClassName, context);
      if (isSubTypeOfClass(optionalTypeInPackage, superClass)) {
        subType = optionalTypeInPackage;
        break;
      }
    }
    return subType;
  }

  private String packagePrefixFromMap(Class<?> superClass) {
    return superClassToPackagePrefixMap().get(superClass);
  }
  
  private JavaType optionalTypeInPackageWith(String packagePrefix, String simpleClassName, DatabindContext context) throws IOException {
    StringBuilder sb = new StringBuilder(packagePrefix.length() + simpleClassName.length());
    sb.append(packagePrefix).append(simpleClassName);
    String fullyQualifiedClassName = sb.toString();
    JavaType typeInSamePackage = context.resolveSubType(_baseType, fullyQualifiedClassName);

    return typeInSamePackage;
  }
  
  private boolean isSubTypeOfClass(JavaType aType, Class<?> superClass) {
    return aType != null && Classes.isSubClass(aType.getRawClass(), superClass);
  }
  
  @Override
  public Id getMechanism() {
    return Id.CUSTOM;
  }

  private Collection<Class<?>> superClasses() {
    return superClasses;
  }
  
  private void setSuperClasses(Collection<Class<?>> superClasses) {
    this.superClasses = superClasses;
  }

  private Map<Class<?>, String> superClassToPackagePrefixMap() {
    return superClassToPackagePrefixMap;
  }

  private void setSuperClassToPackagePrefixMap(Map<Class<?>, String> superClassToPackagePrefixMap) {
    this.superClassToPackagePrefixMap = superClassToPackagePrefixMap;
  }
}
