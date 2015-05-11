package com.ericdcobb.maestroService.api;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.fest.assertions.Assertions;
import org.junit.Test;

/**
 *   
 *
 * @author Eric Cobb on 5/9/15.
 */
public class RouteTest {

	@Test
	public void testDeserialization() throws IOException {
		ObjectMapper mapper = new ObjectMapper();

		Route route = mapper.readValue(this.getClass().getResourceAsStream("/route.json"), Route.class);

		Assertions.assertThat(route.path()).isEqualTo("test");
	}
}
