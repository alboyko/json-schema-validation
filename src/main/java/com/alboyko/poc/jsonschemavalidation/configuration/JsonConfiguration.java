package com.alboyko.poc.jsonschemavalidation.configuration;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.json.JSONObject;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Configuration
public class JsonConfiguration {

    @Bean(name = "jsonSchemaProperties")
    @ConfigurationProperties(prefix = "json.schemas.available")
    public List<String> jsonSchemaProperties() {
        return new ArrayList<>();
    }

    @Bean
    public Module jsonObjectDeserializer() {
        SimpleModule module = new SimpleModule();
        module.addDeserializer(JSONObject.class, new JsonObjectDeserializer());
        return module;
    }

    private class JsonObjectDeserializer extends JsonDeserializer<JSONObject> {
        @Override
        public JSONObject deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            Map<String, Object> bean = p.readValueAs(new TypeReference<Map<String, Object>>() {
            });
            return new JSONObject(bean);
        }
    }
}
