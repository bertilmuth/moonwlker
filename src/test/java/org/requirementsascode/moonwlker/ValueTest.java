package org.requirementsascode.moonwlker;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.junit.jupiter.api.Test;
import org.requirementsascode.moonwlker.paramnames.AdaptedParameterNamesAnnotationIntrospector;
import org.requirementsascode.moonwlker.paramnames.ParameterExtractor;
import org.requirementsascode.moonwlker.testobject.animal.Named;
import org.requirementsascode.moonwlker.testobject.animal.ObjectWithJsonValue;
import org.requirementsascode.moonwlker.testobject.animal.OrphanAnimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(MoonwlkerModule.builder().build());
    objectMapper.registerModule(getValueModule());
    return objectMapper;
  }

  private Module getValueModule() {
    return new SimpleModule() {
      @Override
      public void setupModule(SetupContext context) {
        super.setupModule(context);
        context.insertAnnotationIntrospector(new AdaptedParameterNamesAnnotationIntrospector(new ParameterExtractor()));
        try {
          var method = Named.class.getDeclaredMethod("getName");
          context.insertAnnotationIntrospector(new ValueTypeConfigurer(method));
        } catch (NoSuchMethodException e) {
          e.printStackTrace();
        }
      }
    };
  }
}
