package com.eduardo.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class Json {

  private static final ObjectMapper objectMapper = new ObjectMapper();

  public static <T> String serialize(T obj) throws JsonProcessingException {
    return objectMapper.writeValueAsString(obj);
  }

  public static <T> T deserialize(String json, Class<T> clazz) throws IOException {
    return objectMapper.readValue(json, clazz);
  }
}