package com.ericdcobb.maestroService;

import static com.google.common.io.Resources.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.nio.charset.Charset;
import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ericdcobb.maestroService.api.Route;
import com.ericdcobb.maestroService.services.RouteService;
import com.google.common.io.Resources;

import org.junit.Before;
import org.junit.Test;

/**
 *   
 *
 * @author Eric Cobb on 5/9/15.
 */
public class HandlerTest {

	Handler handler;
	ScriptEngine engine;
	Bindings bindings;
	RouteService routeService;

	@Before
	public void beforeEach() {
		engine = mock(ScriptEngine.class);
		routeService = mock(RouteService.class);

		handler = new Handler(engine, routeService);

		bindings = mock(Bindings.class);
		when(engine.createBindings()).thenReturn(bindings);
	}

	@Test
	public void testHandle() throws IOException, ServletException, ScriptException {
		String expectedScript = Resources.toString(getResource("call-through.groovy"), Charset.defaultCharset());

		org.eclipse.jetty.server.Request request = mock(org.eclipse.jetty.server.Request.class);
		HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
		HttpServletResponse httpServletResponse = mock(HttpServletResponse.class);

		when(routeService.getRoute("/test")).thenReturn(Route.builder().setPath("/test").addVariables("url",
				"http://localhost:8080").setScript("prinln foo").build());

		handler.handle("/test", request, httpServletRequest, httpServletResponse);

		verify(bindings).put("request", httpServletRequest);
		verify(bindings).put("response", httpServletResponse);
		verify(bindings).put("url", "http://localhost:8080");

		verify(engine).eval(expectedScript, bindings);

	}

}
