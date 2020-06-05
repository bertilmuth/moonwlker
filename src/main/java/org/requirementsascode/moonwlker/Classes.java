package org.requirementsascode.moonwlker;

import java.util.Collection;

class Classes {
  public static boolean isSubClassOfAny(Class<?> potentialSubclass, Collection<Class<?>> superClasses) {
    return superClasses.stream()
        .anyMatch(sC -> sC.isAssignableFrom(potentialSubclass));
  }
}
