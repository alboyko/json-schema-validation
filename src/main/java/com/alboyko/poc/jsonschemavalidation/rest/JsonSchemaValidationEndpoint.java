package com.alboyko.poc.jsonschemavalidation.rest;

import com.alboyko.poc.jsonschemavalidation.configuration.JsonSchemaValidator;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JsonSchemaValidationEndpoint {

    @Autowired
    private JsonSchemaValidator jsonSchemaValidator;

    @PostMapping("/validate")
    public void validate(@RequestBody JSONObject jsonInput) {
        jsonSchemaValidator.validate(jsonInput);
    }

}
