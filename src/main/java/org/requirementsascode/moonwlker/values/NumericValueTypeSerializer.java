package org.requirementsascode.moonwlker.values;


import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.NumberSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.util.function.Function;

import static java.util.Objects.requireNonNull;

@SuppressWarnings("serial")
public class NumericValueTypeSerializer<T, N extends Number> extends StdSerializer<T> {

    private static final NumberSerializer numberSerializer = new NumberSerializer(Number.class);

    private final Function<T, N> serializer;

    public NumericValueTypeSerializer(Class<T> t, Function<T, N> serializer) {
        super(t);
        this.serializer = requireNonNull(serializer, "serializer must be non-null!");
    }

    @Override
    public void serialize(T value, JsonGenerator jgen, SerializerProvider provider)
            throws IOException {
        N valueAsNumber = serializer.apply(value);
        numberSerializer.serialize(valueAsNumber, jgen, provider);
    }
}
