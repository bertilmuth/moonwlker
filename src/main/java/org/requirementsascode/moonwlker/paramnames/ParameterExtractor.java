package org.requirementsascode.moonwlker.paramnames;

import java.lang.reflect.Executable;
import java.lang.reflect.Parameter;

public class ParameterExtractor implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    public Parameter[] getParameters(Executable executable) {
        return executable.getParameters();
    }
}
