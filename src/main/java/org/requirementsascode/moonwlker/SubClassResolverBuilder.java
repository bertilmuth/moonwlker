package org.requirementsascode.moonwlker;

import static org.requirementsascode.moonwlker.Classes.isSubClassOfAny;

import java.util.Collection;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTypeResolverBuilder;
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;

class SubClassResolverBuilder extends DefaultTypeResolverBuilder {
  private static final long serialVersionUID = 1L;
  private Collection<Class<?>> superClasses;

  public SubClassResolverBuilder(Collection<Class<?>> superClasses, PolymorphicTypeValidator ptv) {
    super(DefaultTyping.NON_FINAL, ptv);
    setSuperClasses(superClasses);
  }

  @Override
  public boolean useForType(JavaType t) {
    final Class<?> currentClass = t.getRawClass();
    final boolean isSubclassOfAnySuperclass = isSubClassOfAny(currentClass, superClasses());
    return t.isJavaLangObject() || isSubclassOfAnySuperclass;
  }

  private Collection<Class<?>> superClasses() {
    return superClasses;
  }

  private void setSuperClasses(Collection<Class<?>> superClasses) {
    this.superClasses = superClasses;
  }
}