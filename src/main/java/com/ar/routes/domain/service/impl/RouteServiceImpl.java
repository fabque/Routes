package com.ar.routes.domain.service.impl;

import com.ar.routes.domain.model.Route;
import com.ar.routes.domain.model.Station;
import com.ar.routes.domain.model.dto.CreateRouteDto;
import com.ar.routes.domain.repository.RouteRepository;
import com.ar.routes.domain.repository.StationRepository;
import com.ar.routes.domain.service.RouteService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RouteServiceImpl implements RouteService {

    @Autowired
    private final RouteRepository repository;

    @Autowired
    private final StationRepository stationRepository;

    @Override
    public Route create(CreateRouteDto routeDto) throws BadRequestException {
        Station origin =  stationRepository.findById(routeDto.getOrigin()).orElseThrow(() -> new BadRequestException("Origin Not Found"));
        Station destination =  stationRepository.findById(routeDto.getDestination()).orElseThrow(() -> new BadRequestException("Destination Not Found"));
        Route newRoute = Route.builder().origin(origin).destination(destination).cost(routeDto.getCost()).build();
        return repository.save(newRoute);
    }
}
