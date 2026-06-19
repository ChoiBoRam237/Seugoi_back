package com.example.seugoi_back.Util;

import lombok.extern.slf4j.Slf4j;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.util.Collections;
import java.util.List;

@Slf4j
public class ListUtil {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    public static List<String> parseStringList(String value) {
        try {
            if (value == null || value.isBlank()) {
                return Collections.emptyList();
            }
            return objectMapper.readValue(value, new TypeReference<List<String>>() {});
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }
}
