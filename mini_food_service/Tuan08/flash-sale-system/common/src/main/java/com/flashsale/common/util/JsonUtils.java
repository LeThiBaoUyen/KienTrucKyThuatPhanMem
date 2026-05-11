package com.flashsale.common.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * JSON Utility
 */
public class JsonUtils {
    private static final Gson gson = new GsonBuilder()
            .serializeNulls()
            .create();

    public static String toJson(Object obj) {
        return gson.toJson(obj);
    }

    public static <T> T fromJson(String json, Class<T> clazz) {
        return gson.fromJson(json, clazz);
    }
}
