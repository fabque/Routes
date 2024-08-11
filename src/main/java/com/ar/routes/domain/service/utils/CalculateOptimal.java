package com.ar.routes.domain.service.utils;

import com.ar.routes.domain.model.Route;
import com.ar.routes.domain.model.Station;

import java.util.*;

public class CalculateOptimal {
    /**
     * Implemented using Dijkstra algorithm
     * @param origin
     * @param destiny
     * @param stationList
     * @param routeList
     * @return
     */
    public static List<Station> getOptimalRoute(Station origin, Station destiny, List<Station> stationList, List<Route> routeList) {
        if (routeList.isEmpty()) {
            return new ArrayList<>();
        }
        Map<Station, Double> costs = new HashMap<>();
        Map<Station, Station> predecesors = new HashMap<>();
        PriorityQueue<Station> queue = new PriorityQueue<>(Comparator.comparingDouble(costs::get));

        stationList.forEach(station -> {
            costs.put(station, Double.MAX_VALUE);
        });
        costs.put(origin, (double) 0);

        queue.add(origin);

        while (!queue.isEmpty()) {
            Station actual = queue.poll();

            if (actual.equals(destiny)) {
                break;
            }

            for (Route route : routeList) {
                /**
                 * Examine origen and destination ways (bidirectional)
                 */
                if (route.getOrigin().equals(actual) || route.getDestination().equals(actual)) {
                    Station neighbour = (route.getOrigin().equals(actual)) ? route.getDestination() : route.getOrigin();
                    double newCost = costs.get(actual) + route.getCost();

                    if (newCost < costs.get(neighbour)) {
                        costs.put(neighbour, newCost);
                        predecesors.put(neighbour, actual);
                        queue.add(neighbour);
                    }
                }
            }
        }

        List<Station> rutaOptima = new LinkedList<>();
        Station step = destiny;

        if (predecesors.get(step) == null) {
            return null;
        }

        while (step != null) {
            rutaOptima.add(0, step);
            step = predecesors.get(step);
        }

        return rutaOptima;
    }
}
