package com.ericdcobb;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.eclipse.jetty.server.Server;

/**
 * Hello world!
 */
public class MaestroService
{
	public static void main(String[] args) throws Exception {
		ScriptEngineManager factory = new ScriptEngineManager();
		ScriptEngine engine = factory.getEngineByName("groovy");

		Server server = new Server(3000);

		server.setHandler(new Handler(engine));
		server.start();
		server.dumpStdErr();
		server.join();
	}
}
