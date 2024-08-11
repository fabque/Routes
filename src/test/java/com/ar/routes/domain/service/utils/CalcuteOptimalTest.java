package com.ar.routes.domain.service.utils;

import com.ar.routes.domain.model.Route;
import com.ar.routes.domain.model.Station;
import com.ar.routes.domain.model.dto.RouteResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

public class CalcuteOptimalTest{

    @Test
    public void testGetOptimalRoute_Success() {
        Station origin = new Station(1L, "A");
        Station destination = new Station(2L, "B");
        Station intermediate = new Station(3L, "C");

        List<Station> stationList = List.of(origin, destination, intermediate);
        List<Route> routeList = List.of(
                new Route(1L, origin, intermediate, 2),
                new Route(2L, intermediate, destination, 2)
        );

        RouteResponse result = CalculateOptimal.getOptimalRoute(origin, destination, stationList, routeList);

        List<Long> expectedRoute = List.of(origin.getId(), intermediate.getId(), destination.getId());
        Assertions.assertEquals(expectedRoute, result.getPath());
    }

    @Test
    public void testGetOptimalRoute_NoRouteFound() {
        Station origin = new Station(1L, "A");
        Station destination = new Station(2L, "B");
        Station isolated = new Station(3L, "C");

        List<Station> stationList = List.of(origin, destination, isolated);
        List<Route> routeList = List.of(
                new Route(1L, origin, isolated, 2) // No route to destination
        );

        RouteResponse result = CalculateOptimal.getOptimalRoute(origin, destination, stationList, routeList);

        Assertions.assertNull(result, "Expected no route found");
    }

    @Test
    public void testGetOptimalRoute_EmptyRouteList() {
        Station origin = new Station(1L, "A");
        Station destination = new Station(2L, "B");

        List<Station> stationList = List.of(origin, destination);
        List<Route> routeList = new ArrayList<>(); // No routes available

        RouteResponse result = CalculateOptimal.getOptimalRoute(origin, destination, stationList, routeList);

        Assertions.assertTrue(result.getPath().isEmpty(), "Expected an empty list when no routes are available");
    }


}
