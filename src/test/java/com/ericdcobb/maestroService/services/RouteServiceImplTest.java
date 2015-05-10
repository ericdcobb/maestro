package com.ericdcobb.maestroService.services;

import static org.fest.assertions.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Map;

import com.ericdcobb.maestroService.api.Route;

import org.junit.Before;
import org.junit.Test;

/**
 *   
 *
 * @author Eric Cobb on 5/9/15.
 */
public class RouteServiceImplTest {

	RouteService routeService;

	@Before
	public void setUp() {
		routeService = new RouteServiceImpl();
	}

	@Test
	public void testSave() {
		@SuppressWarnings("unchecked")
		Route savedRoute = routeService.saveRoute(Route.builder().setPath("/test").setVariables(mock(Map.class))
				.setScript("prinln foo")
				.build
				());
		assertThat(savedRoute).isEqualTo(routeService.getRoute("/test"));

	}

	@Test
	public void testSaveMultiple() {
		@SuppressWarnings("unchecked")
		Route savedRoute = Route.builder().setPath("/test").setVariables(mock(Map.class))
				.setScript("prinln foo")
				.build();

		@SuppressWarnings("unchecked")
		Route savedRoute2 = Route.builder().setPath("/foo").addVariables("Test", new Object())
				.setScript("prinln foo")
				.build();

		routeService.saveRoutes(savedRoute, savedRoute2);

		assertThat(savedRoute).isEqualTo(routeService.getRoute("/test"));
		assertThat(savedRoute2).isEqualTo(routeService.getRoute("/foo"));

	}
}
