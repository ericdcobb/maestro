package com.ericdcobb.maestroService;

import static com.google.common.io.Resources.*;
import static java.nio.charset.Charset.*;

import java.io.File;
import java.io.IOException;
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

import org.apache.commons.io.FileUtils;
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
						map -> {
							Route route = null;
							try {
								route = Route.builder()
									.setScript(map.get("script").equals("call-through") ? callThrough :
													FileUtils.readFileToString(new File(map.get("script").toString()))
									).setPath(map.get("path").toString()).
									setVariables((Map) map.get("variables")).
									build();
							} catch (IOException e) {
								e.printStackTrace();
							}
							return route;
						
						})
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
