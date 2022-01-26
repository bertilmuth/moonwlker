package org.requirementsascode.moonwlker.values;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.util.function.Function;

import static java.util.Objects.requireNonNull;

@SuppressWarnings("serial")
public class NumericValueTypeDeserializer<T, N extends Number> extends StdDeserializer<T> {
    private final Function<N, T> deserializer;

    public NumericValueTypeDeserializer(Class<T> valueType, Function<N, T> deserializer) {
        super(valueType);
        this.deserializer = requireNonNull(deserializer, "deserializer must be non-null!");
    }

    @Override
    public T deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        @SuppressWarnings("unchecked")
        N numericValue = (N) jp.readValueAs(Number.class);
        return deserializer.apply(numericValue);
    }
}
