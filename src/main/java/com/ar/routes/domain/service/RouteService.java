package com.ar.routes.domain.service;

import com.ar.routes.domain.model.Route;
import com.ar.routes.domain.model.Station;
import com.ar.routes.domain.model.dto.CreateRouteDto;
import com.ar.routes.domain.model.dto.RouteResponse;
import org.apache.coyote.BadRequestException;

import java.util.List;

public interface RouteService {

    Route create(CreateRouteDto routeDto) throws BadRequestException;

    List<Route> getRoutes();

    void deleteRoute(Long id) throws BadRequestException;

    RouteResponse getOptimalRoute(Long origen, Long destiny) throws BadRequestException;

    void initDB();
}