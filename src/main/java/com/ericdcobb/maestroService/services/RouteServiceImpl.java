package com.ericdcobb.maestroService.services;

import static java.util.Arrays.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.ericdcobb.maestroService.api.Route;

/**
 *   
 *
 * @author Eric Cobb on 5/9/15.
 */
public class RouteServiceImpl implements RouteService {

	Map<String, Route> routeMap = new ConcurrentHashMap<>();

	@Override
	public Route saveRoute(Route route) {
		routeMap.put(route.path(), route);

		return route;
	}

	@Override
	public List<Route> saveRoutes(Route... routes) {
		List<Route> routesList = asList(routes);
		asList(routes).forEach(route -> routeMap.put(route.path(), route));
		return routesList;
	}

	@Override
	public Route getRoute(String path) {
		return routeMap.get(path);
	}
}
