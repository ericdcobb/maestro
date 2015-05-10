package com.ericdcobb.maestroService;

import static com.google.common.io.Resources.*;
import static java.nio.charset.Charset.*;
import static java.util.Arrays.*;

import java.io.IOException;
import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
import javax.script.SimpleScriptContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ericdcobb.maestroService.api.Route;
import com.ericdcobb.maestroService.services.RouteService;
import com.google.common.io.Resources;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

/**
 *   
 *
 * @author Eric Cobb on 5/9/15.
 */
public class Handler extends AbstractHandler {

	final ScriptEngine engine;
	final RouteService routeService;

	public Handler(ScriptEngine engine, RouteService routeService) {
		this.engine = engine;
		this.routeService = routeService;
	}

	@Override
	public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String key = asList(target.split("\\/")).stream().filter(part -> !part.isEmpty()).findFirst().get();

		ScriptContext newContext = new SimpleScriptContext();
		newContext.setBindings(engine.createBindings(), ScriptContext.ENGINE_SCOPE);
		Bindings engineScope = newContext.getBindings(ScriptContext.ENGINE_SCOPE);

		engineScope.put("request", request);
		engineScope.put("response", response);

		Route route = routeService.getRoute(key);
		route.variables().entrySet().stream().forEach(entry -> engineScope
				.put(entry.getKey(), entry.getValue()));
		try {
			engine.eval(route.script(), engineScope);
		}
		catch (ScriptException e) {
			e.printStackTrace();
		}
	}
}
