package com.wisd.dbs.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * date: 2018-09-07
 * time: 15:55
 *
 * @author wqy
 */
@Component
public class JsonUtil {
    private static ObjectMapper objectMapper;
    private static Gson gson = new GsonBuilder().setPrettyPrinting().create();

    /**
     * 此方法用于一般对象的转化，带有{@http java.time.LocalDate}的对象不能用此方法，会造成内存益出
     *
     * @param obj
     * @return
     */
    @SneakyThrows
    public static String toJson(Object obj) {
        return objectMapper.writeValueAsString(obj);
    }

    public static String toJsonGson(Object obj) {
        return gson.toJson(obj);
    }

    @SneakyThrows
    public static <T> T fromJsonGson(String str, Class<T> type) {
        return gson.fromJson(str, type);
    }

    public static <T> T fromJsonGson(String string, Type type) {
        return gson.fromJson(string, type);
    }

    @SneakyThrows
    public static <T> T fromJson(String str, Class<T> type) {
        return objectMapper.readValue(str, type);
    }

    @SneakyThrows
    public static Map<String, Object> parseToMap(String str) {
        TypeReference<Map<String, Object>> typeref =
                new TypeReference<Map<String, Object>>() {};
        return objectMapper.readValue(str, typeref);
    }

    @SneakyThrows
    static <T> List<T> parseToList(String str) {
        TypeReference<List<T>> typeref = new TypeReference<List<T>>() {};
        return objectMapper.readValue(str, typeref);
    }

    @Autowired
    public void confObjectMapper(ObjectMapper mapper) {
        objectMapper = mapper;
    }


}
