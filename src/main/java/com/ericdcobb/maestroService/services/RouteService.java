package com.ericdcobb.maestroService.services;

import java.util.List;

import com.ericdcobb.maestroService.api.Route;

/**
 *   
 *
 * @author Eric Cobb on 5/9/15.
 */
public interface RouteService {

	Route saveRoute(Route route);

	List<Route> saveRoutes(Route ... routes);

	Route getRoute(String path);
}
