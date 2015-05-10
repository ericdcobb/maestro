package com.ericdcobb.maestroService;

import static com.google.common.io.Resources.*;
import static java.nio.charset.Charset.*;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import com.ericdcobb.maestroService.api.Route;
import com.ericdcobb.maestroService.services.RouteService;
import com.ericdcobb.maestroService.services.RouteServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Resources;

import org.eclipse.jetty.server.Server;

/**
 * Hello world!
 */
public class MaestroService
{
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {

		ObjectMapper mapper = new ObjectMapper();

		List<Map<String, Object>> routeList = mapper.readValue(new File(args[0]), List.class);
		RouteService routeService = new RouteServiceImpl();

		String callThrough = Resources.toString(getResource("call-through.groovy"), defaultCharset());

		routeService.saveRoutes(
				routeList.stream().map(
						map -> Route.builder()
								.setScript(map.get("script").equals("call-through") ? callThrough : map.get("script")
										.toString()).
								setPath(map.get("path").toString()).
								setVariables((Map) map.get("variables")).
								build())
						.collect(Collectors.toList()).toArray(new Route[routeList.size()]));

		ScriptEngineManager factory = new ScriptEngineManager();
		ScriptEngine engine = factory.getEngineByName("groovy");

		Server server = new Server(3000);

		server.setHandler(new Handler(engine, routeService));
		server.start();
		server.dumpStdErr();
		server.join();
	}
}
