package io.ark.core.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.ark.core.config.ConfigLoader;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

public final class ResponseUtils {

  private static final long TIMEOUT_LENGTH = 5;
  private static final TimeUnit TIMEOUT_UNITS = TimeUnit.SECONDS;
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

  public static <T> T getObjectFromJson(String object, Class<T> clazz) {
    try {
      return objectMapper.readValue(object, clazz);
    } catch (IOException e) {
      e.printStackTrace();
      throw new IllegalArgumentException(
          "JSON could not be resolved to class " + clazz.getSimpleName());
    }
  }

  public static <T> T getObjectFromJson(JSONObject object, Class<T> clazz) {
    return getObjectFromJson(object.toString(), clazz);
  }

  public static String getObjectAsJson(Object obj) {
    try {
      return objectMapper.writeValueAsString(obj);
    } catch (JsonProcessingException e) {
      throw new IllegalArgumentException("Object could not be converted to JSON");
    }
  }

  public static <T> T getResponse(Future<String> response, Class<T> clazz) throws InterruptedException, ExecutionException, TimeoutException {
    String res = response.get(TIMEOUT_LENGTH, TIMEOUT_UNITS);
    return getObjectFromJson(res, clazz);
  }

  private ResponseUtils() {}
}
