package org.requirementsascode.moonwlker.paramnames;

import java.lang.reflect.Executable;
import java.lang.reflect.Parameter;

/**
 * IMPORTANT NOTE from b_muth: This has been adapted from the official ParamNames module to
 * be able to deal with all argument constructors without JsonCreator
 * annotation.
 * 
 * See https://github.com/FasterXML/jackson-modules-java8/tree/master/parameter-names for the original module.
 */
public class ParameterExtractor implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Get the parameters
     * 
     * @param executable the exectutable
     * @return the parameters array
     */
    public Parameter[] getParameters(Executable executable) {
        return executable.getParameters();
    }
}
