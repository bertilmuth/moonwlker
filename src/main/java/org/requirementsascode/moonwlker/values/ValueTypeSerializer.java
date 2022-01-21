package org.requirementsascode.moonwlker.values;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.util.function.Function;

import static java.util.Objects.requireNonNull;

public class ValueTypeSerializer<T> extends StdSerializer<T> {
    private final Function<T, String> valueToString;

    public ValueTypeSerializer(Class<T> t, Function<T, String> valueToString) {
        super(t);
        this.valueToString = requireNonNull(valueToString, "valueToString must be non-null!");
    }

    @Override
    public void serialize(T value, JsonGenerator jgen, SerializerProvider provider)
            throws IOException {
        String valueAsString = valueToString.apply(value);
        jgen.writeString(valueAsString);
    }
}
