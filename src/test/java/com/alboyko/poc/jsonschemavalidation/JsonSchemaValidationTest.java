package com.alboyko.poc.jsonschemavalidation;

import com.alboyko.poc.jsonschemavalidation.utils.JsonResourceUtils;
import org.everit.json.schema.EmptySchema;
import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

public class JsonSchemaValidationTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    private static Schema identifiableSchema;
    private static Schema navigableSchema;
    private static Schema compositeSchema;

    @BeforeClass
    public static void init() throws IOException {
        identifiableSchema = loadAndValidateSchema("schema/identifiable-schema.json");
        navigableSchema = loadAndValidateSchema("schema/navigable-schema.json");
        compositeSchema = loadAndValidateSchema("schema/composite-item-schema.json");
    }

    private static Schema loadAndValidateSchema(String path) throws IOException {
        JSONObject jsonSchema = JsonResourceUtils.getResourceAsJsonObject(path);
        Schema schema = SchemaLoader.load(jsonSchema);
        assertThat(schema, not(instanceOf(EmptySchema.class)));
        return schema;
    }

    @Test
    public void testIdentifiable() throws IOException {
        JSONObject jsonInput = JsonResourceUtils.getResourceAsJsonObject("identifiable-item.json");
        identifiableSchema.validate(jsonInput);
    }

    @Test
    public void testIdentifiableMissingRequired() throws IOException {
        JSONObject jsonInput = JsonResourceUtils.getResourceAsJsonObject("identifiable-missing-required.json");

        exception.expect(ValidationException.class);

        identifiableSchema.validate(jsonInput);
    }

    @Test
    public void testNavigable() throws IOException {
        JSONObject jsonInput = JsonResourceUtils.getResourceAsJsonObject("navigable-item.json");
        navigableSchema.validate(jsonInput);
    }

    @Test
    public void testComposite() throws IOException {
        JSONObject jsonInput = JsonResourceUtils.getResourceAsJsonObject("item-valid.json");
        compositeSchema.validate(jsonInput);
    }

    @Test
    public void testCompositeMissingRequired() throws IOException {
        JSONObject jsonInput = JsonResourceUtils.getResourceAsJsonObject("item-missing-required.json");

        exception.expect(ValidationException.class);

        compositeSchema.validate(jsonInput);
    }

}
