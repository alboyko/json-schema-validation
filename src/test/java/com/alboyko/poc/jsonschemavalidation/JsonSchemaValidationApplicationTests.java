package com.alboyko.poc.jsonschemavalidation;

import com.alboyko.poc.jsonschemavalidation.utils.JsonResourceUtils;
import org.everit.json.schema.ValidationException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.isA;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class JsonSchemaValidationApplicationTests {

	@Rule
	public final ExpectedException exception = ExpectedException.none();

	@Autowired
	private MockMvc mvc;

	@Test
	public void contextLoads() {
		assertNotNull(mvc);
	}

	@Test
	public void validated() throws Exception {
		mvc.perform(post("/validate")
				.content(JsonResourceUtils.getResourceAsString("item-valid.json"))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}

	@Test
	public void missingRequired() throws Exception {
		exception.expectCause(isA(ValidationException.class));

		mvc.perform(post("/validate")
				.content(JsonResourceUtils.getResourceAsString("item-missing-required.json"))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError());
	}

}
