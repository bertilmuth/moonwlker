package org.requirementsascode.moonwlker;

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.function.Function;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.requirementsascode.moonwlker.testobject.animal.ObjectWithJsonValue;
import org.requirementsascode.moonwlker.testobject.animal.OrphanAnimal;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class ValueTest extends MoonwlkerModuleTest {

  @Test
  public void reads_oneValue() throws Exception {
    ObjectMapper objectMapper = getObjectMapper();

    String jsonString = "\"Boo\"";
    OrphanAnimal orphanAnimal = objectMapper.readValue(jsonString, OrphanAnimal.class);
    assertEquals("Boo", orphanAnimal.getName());
  }

  @Test
  public void writes_oneJsonValue() throws Exception {
    ObjectMapper objectMapper = getObjectMapper();

    OrphanAnimal orphanAnimal = new OrphanAnimal("Boo");
    assertEquals("\"Boo\"", writeToJson(objectMapper, orphanAnimal));
  }

  @Test
  public void reads_oneObjectWithJsonValue() throws Exception {
    ObjectMapper objectMapper = getObjectMapper();


    String jsonString = "{\"someString\":\"blah\",\"orphanAnimal\":\"Boo\"}";
    var object = objectMapper.readValue(jsonString, ObjectWithJsonValue.class);
    assertEquals("Boo", object.getOrphanAnimal().getName());
  }

  @Test
  public void writes_oneObjectWithJsonValue() throws Exception {
    ObjectMapper objectMapper = getObjectMapper();

    OrphanAnimal orphanAnimal = new OrphanAnimal("Boo");
    var object = new ObjectWithJsonValue("blah", orphanAnimal);
    assertEquals("{\"someString\":\"blah\",\"orphanAnimal\":\"Boo\"}", writeToJson(objectMapper, object));
  }
  
  private ObjectMapper getObjectMapper() {
    return getObjectMapper(OrphanAnimal.class, OrphanAnimal::getName, OrphanAnimal::new);
  }

  private <T> ObjectMapper getObjectMapper(Class<T> valueType, Function<T, String> valueToString, Function<String, T> stringToValue) {
    ValueTypeSerializer<T> serializer = new ValueTypeSerializer<>(valueType, valueToString);
    ValueTypeDeserializer<T> deserializer = new ValueTypeDeserializer<>(valueType, stringToValue);
    
    MoonwlkerModule module = MoonwlkerModule.builder().build();
    module.addSerializer(valueType, serializer);
    module.addDeserializer(valueType, deserializer);

    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(module);
    return objectMapper;
  }

  @SuppressWarnings("serial")
  class ValueTypeSerializer<T> extends StdSerializer<T> {
    private final Function<T, String> valueToString;

    public ValueTypeSerializer(Class<T> t, Function<T, String> valueToString) {
      super(t);
      this.valueToString = requireNonNull(valueToString, "valueToString must be non-null!");
    }

    @Override
    public void serialize(T value, JsonGenerator jgen, SerializerProvider provider)
        throws IOException, JsonProcessingException {
      String valueAsString = valueToString.apply(value);
      jgen.writeString(valueAsString);
    }
  }
  
  @SuppressWarnings("serial")
  class ValueTypeDeserializer<T> extends StdDeserializer<T> {
    private final Function<String, T> stringToValue;

    public ValueTypeDeserializer(Class<?> valueType, Function<String, T> stringToValue) {
      super(valueType);
      this.stringToValue = requireNonNull(stringToValue, "stringToValue must be non-null!");
    }

    @Override
    public T deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
      String stringValue = jp.getValueAsString();
      T value = stringToValue.apply(stringValue);
      return value;
    }
  }
}
