package io.github.ljezio.jwttoken.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.module.kotlin.KotlinModule;
import lombok.SneakyThrows;

public class JsonUtil {

    private static final ObjectMapper objectMapper = JsonMapper.builder()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
            .build()
            .registerModule(new SimpleModule())
            .registerModule(new KotlinModule.Builder().build());

    @SneakyThrows
    public static <T> String toJson(T payload) {
        return objectMapper.writeValueAsString(payload);
    }

    @SneakyThrows
    public static <T> T toBean(String jsonStr, Class<T> clazz) {
        return objectMapper.readValue(jsonStr, clazz);
    }
}
