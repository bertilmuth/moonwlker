package org.requirementsascode.moonwlker;

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.function.Function;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.requirementsascode.moonwlker.testobject.animal.Named;
import org.requirementsascode.moonwlker.testobject.animal.ObjectWithJsonValue;
import org.requirementsascode.moonwlker.testobject.animal.OrphanAnimal;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class ValueTest extends MoonwlkerModuleTest {

  @Test
  @Disabled
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
  @Disabled
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
    return getObjectMapper(Named.class, Named::getName);
  }

  private <T> ObjectMapper getObjectMapper(Class<T> valueType, Function<T, String> valueToString) {
    ObjectMapper objectMapper = new ObjectMapper();
    MoonwlkerModule module = MoonwlkerModule.builder().build();
    ValueTypeSerializer<T> ser = new ValueTypeSerializer<>(valueType, valueToString);
    
    module.addSerializer(valueType, ser);

    objectMapper.registerModule(module);
    return objectMapper;
  }

  @SuppressWarnings("serial")
  public class ValueTypeSerializer<T> extends StdSerializer<T> {
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
}
