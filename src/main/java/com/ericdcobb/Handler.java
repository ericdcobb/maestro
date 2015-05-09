package com.ericdcobb;

import static com.google.common.io.Resources.getResource;
import static java.nio.charset.Charset.defaultCharset;

import java.io.IOException;
import java.nio.charset.Charset;
import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
import javax.script.SimpleScriptContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

	public Handler(ScriptEngine engine) {
		this.engine = engine;
	}

	@Override
	public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		ScriptContext newContext = new SimpleScriptContext();
		newContext.setBindings(engine.createBindings(), ScriptContext.ENGINE_SCOPE);
		Bindings engineScope = newContext.getBindings(ScriptContext.ENGINE_SCOPE);

		engineScope.put("request", request);
		engineScope.put("response", response);
		engineScope.put("url", "http://localhost:8080");

		try {
			engine.eval(Resources.toString(getResource("call-through.groovy"), defaultCharset()), engineScope);
		}
		catch (ScriptException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
}
