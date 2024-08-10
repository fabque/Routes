package com.ar.routes.domain.service;

import com.ar.routes.domain.model.Route;
import com.ar.routes.domain.model.dto.CreateRouteDto;
import org.apache.coyote.BadRequestException;

public interface RouteService {

    Route create(CreateRouteDto routeDto) throws BadRequestException;


}