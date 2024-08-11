package com.ar.routes.domain.service;

import com.ar.routes.domain.model.Route;
import com.ar.routes.domain.model.Station;
import com.ar.routes.domain.model.dto.CreateRouteDto;
import org.apache.coyote.BadRequestException;

import java.util.List;

public interface RouteService {

    Route create(CreateRouteDto routeDto) throws BadRequestException;

    List<Station> getOptimalRoute(Integer origen, Integer destiny) throws BadRequestException;

    void initDB();

    List<Route> getRoutes();
}