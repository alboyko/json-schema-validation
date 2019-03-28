package com.alboyko.poc.jsonschemavalidation.configuration;

import com.alboyko.poc.jsonschemavalidation.utils.JsonResourceUtils;
import lombok.extern.slf4j.Slf4j;
import org.everit.json.schema.Schema;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
class JsonSchemaRegistry {

    @Autowired
    @Qualifier("jsonSchemaProperties")
    private List<String> schemaPaths;

    @Value("${json.schemas.default}")
    String defaultSchemaPath;

    private Map<String, Schema> schemas = new HashMap<>();
    private Schema defaultSchema;

    @PostConstruct
    void init() {
        schemaPaths.forEach(this::registerSchema);
        defaultSchema = getSchema(defaultSchemaPath);
    }

    private boolean registerSchema(String path) {
        try {
            JSONObject json = JsonResourceUtils.getResourceAsJsonObject(path);
            Schema schema = SchemaLoader.load(json);
            schemas.put(schema.getId(), schema);
        } catch (IOException e) {
            log.error(String.format("Cannot register schema %s", path), e);
            return false;
        }

        return true;
    }

    Schema getSchema(String schemaName) {
        String schemaId = getSchemaId(schemaName);

        return schemas.entrySet().stream()
                .filter(entry -> entry.getKey().contains(schemaId))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse(defaultSchema);
    }

    private String getSchemaId(String schemaName) {
        int lastPathIndex = schemaName.lastIndexOf("/");
        return lastPathIndex == -1 ? schemaName : schemaName.substring(lastPathIndex);
    }

}
