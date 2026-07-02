package com.example.seugoi_back.Util;

import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.util.Collections;
import java.util.List;

public class ListUtil {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    // string -> list 변환 util
    public static List<String> parseStringToList(String value) {
        try {
            if (value == null || value.isBlank()) {
                return Collections.emptyList();
            }
            return objectMapper.readValue(value, new TypeReference<List<String>>() {});
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    // list -> string 변환 util
    public static String parseListToString(List<String> values) {
        return objectMapper.writeValueAsString(values);
    }
}
