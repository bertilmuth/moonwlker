package org.requirementsascode.moonwlker.values;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.util.function.Function;

import static java.util.Objects.requireNonNull;

public class ValueTypeDeserializer<T> extends StdDeserializer<T> {
    private final Function<String, T> stringToValue;

    public ValueTypeDeserializer(Class<T> valueType, Function<String, T> stringToValue) {
        super(valueType);
        this.stringToValue = requireNonNull(stringToValue, "stringToValue must be non-null!");
    }

    @Override
    public T deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        String stringValue = jp.getValueAsString();
        T value = stringToValue.apply(stringValue);
        return value;
    }
}
