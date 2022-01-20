package org.requirementsascode.moonwlker;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.AnnotatedConstructor;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class ValueTypeConfigurer extends AnnotationIntrospector {
    private final Method valueMethod;

    public ValueTypeConfigurer(Method valueMethod) {
        this.valueMethod = valueMethod;
    }

    @Override
    public boolean hasCreatorAnnotation(Annotated ann) {
        return ann instanceof AnnotatedConstructor
                && ((AnnotatedConstructor) ann).getParameterCount() == 1;
    }

    @Override
    public JsonCreator.Mode findCreatorBinding(Annotated ann) {
        return JsonCreator.Mode.DELEGATING;
    }

    @Override
    protected boolean _hasAnnotation(Annotated ann, Class<? extends Annotation> annoClass) {
        return super._hasAnnotation(ann, annoClass);
    }

    @Override
    public Boolean hasAsValue(Annotated a) {
        if (a instanceof AnnotatedMethod) {
            Class<?> methodClass = ((AnnotatedMethod) a).getDeclaringClass();
            return (valueMethod.getDeclaringClass().isAssignableFrom(methodClass))
                && a.getName().equals(valueMethod.getName());
        }
        return false;
    }

    @Override
    public Version version() {
        return Version.unknownVersion();
    }
}