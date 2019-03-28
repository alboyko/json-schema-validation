package com.alboyko.poc.jsonschemavalidation.configuration;

import lombok.extern.slf4j.Slf4j;
import org.everit.json.schema.Schema;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JsonSchemaValidator {

    public static final String SCHEMA_COMPOSITE = "composite";
    public static final String SCHEMA_NAVIGABLE = "navigable";
    public static final String SCHEMA_IDENTIFIABLE = "identifiable";

    @Autowired
    private JsonSchemaRegistry registry;

    public void validate(@NonNull JSONObject input) {
        Schema schema = getSchema(input);
        schema.validate(input);
    }

    private Schema getSchema(@NonNull JSONObject input) {
        if (isComposite(input)) {
            return registry.getSchema(SCHEMA_COMPOSITE);
        } else if (isNavigable(input)) {
            return registry.getSchema(SCHEMA_NAVIGABLE);
        } else {
            return registry.getSchema(SCHEMA_IDENTIFIABLE);
        }
    }

    private boolean isComposite(@NonNull JSONObject input) {
        return isIdentifiable(input) && isNavigable(input);
    }

    private boolean isNavigable(@NonNull JSONObject input) {
        return input.has("navigable");
    }

    private boolean isIdentifiable(@NonNull JSONObject input) {
        return input.has("uri");
    }

}
