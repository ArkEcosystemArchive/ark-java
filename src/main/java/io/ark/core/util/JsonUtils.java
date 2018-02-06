package io.ark.core.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.ark.core.config.ConfigLoader;

public final class JsonUtils {

  private static final ObjectMapper objectMapper = new ObjectMapper();

  public static JSONObject getResourceJSON(String resource) throws Exception {
    StringWriter writer = new StringWriter();
    InputStream inputStream = ConfigLoader.class.getClassLoader().getResourceAsStream(resource);
    IOUtils.copy(inputStream, writer, StandardCharsets.UTF_8);
    inputStream.close();
    return new JSONObject(writer.toString());
  }

  public static JSONObject getJsonFromString(String resource) {
    return new JSONObject(resource);
  }

  public static <T> T getObjectFromJson(JSONObject object, Class<T> clazz) {
    try {
      return objectMapper.readValue(object.toString(), clazz);
    } catch (IOException e) {
      e.printStackTrace();
      throw new IllegalArgumentException("JSON could not be resolved to class " + clazz.getSimpleName());
    }
  }

  public static String getObjectAsJson(Object obj) {
    try {
      return objectMapper.writeValueAsString(obj);
    } catch (JsonProcessingException e) {
      throw new IllegalArgumentException("Object could not be converted to JSON");
    }
  }

  private JsonUtils() {
  }
}
