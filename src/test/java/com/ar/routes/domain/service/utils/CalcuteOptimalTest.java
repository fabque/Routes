package com.ar.routes.domain.service.utils;

import com.ar.routes.domain.model.Route;
import com.ar.routes.domain.model.Station;
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

        List<Station> result = CalculateOptimal.getOptimalRoute(origin, destination, stationList, routeList);

        List<Station> expectedRoute = List.of(origin, intermediate, destination);
        Assertions.assertEquals(expectedRoute, result);
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

        List<Station> result = CalculateOptimal.getOptimalRoute(origin, destination, stationList, routeList);

        Assertions.assertNull(result, "Expected no route found");
    }

    @Test
    public void testGetOptimalRoute_EmptyRouteList() {
        Station origin = new Station(1L, "A");
        Station destination = new Station(2L, "B");

        List<Station> stationList = List.of(origin, destination);
        List<Route> routeList = new ArrayList<>(); // No routes available

        List<Station> result = CalculateOptimal.getOptimalRoute(origin, destination, stationList, routeList);

        Assertions.assertTrue(result.isEmpty(), "Expected an empty list when no routes are available");
    }


}
