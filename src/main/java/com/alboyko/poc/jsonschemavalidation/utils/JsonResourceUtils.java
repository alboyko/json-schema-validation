package com.alboyko.poc.jsonschemavalidation.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class JsonResourceUtils {
    public static JSONObject getResourceAsJsonObject(String path) throws JSONException, IOException {
        return new JSONObject(new JSONTokener(getResourceAsStream(path)));
    }

    public static JsonNode getResourceAsJsonNode(String path) throws JSONException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readTree(getResourceAsStream(path));
    }

    public static InputStream getResourceAsStream(String path) throws IOException {
        return new ClassPathResource(path).getInputStream();
    }

    public static String getResourceAsString(String path) throws IOException {
        try (BufferedReader br = getReader(path)) {
            return br.lines().collect(Collectors.joining(System.lineSeparator()));
        }
    }

    private static BufferedReader getReader(String path) throws IOException {
        return new BufferedReader(new InputStreamReader(getResourceAsStream(path)));
    }
}
