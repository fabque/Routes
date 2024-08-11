package com.ar.routes.domain.service.impl;

import com.ar.routes.domain.model.dto.RouteResponse;
import com.ar.routes.domain.service.utils.CalculateOptimal;
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

import java.util.*;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RouteServiceImpl implements RouteService {

    public static final String ORIGIN_NOT_FOUND = "Origin Not Found";
    public static final String DESTINATION_NOT_FOUND = "Destination Not Found";
    public static final String COULD_NOT_BE_THE_SAME = "Origin and destination could not be the same";
    public static final String NOT_FOUND = "Not Found";

    @Autowired
    private final RouteRepository repository;

    @Autowired
    private final StationRepository stationRepository;

    @Override
    public Route create(CreateRouteDto routeDto) throws BadRequestException {
        if (routeDto.getOrigin().equals(routeDto.getDestination())) {
            throw new BadRequestException(COULD_NOT_BE_THE_SAME);
        }
        Station origin =  stationRepository.findById(routeDto.getOrigin()).orElseThrow(() -> new BadRequestException(ORIGIN_NOT_FOUND));
        Station destination =  stationRepository.findById(routeDto.getDestination()).orElseThrow(() -> new BadRequestException(DESTINATION_NOT_FOUND));
        Route newRoute = Route.builder().origin(origin).destination(destination).cost(routeDto.getCost()).build();
        return repository.save(newRoute);
    }

    @Override
    public List<Route> getRoutes() {
        return repository.findAll();
    }

    @Override
    public void deleteRoute(Long id) throws BadRequestException {
        Route route = repository.findById(id).orElseThrow(()-> new BadRequestException(NOT_FOUND));
        repository.delete(route);
    }


    @Override
    public RouteResponse getOptimalRoute(Long origen, Long destiny) throws BadRequestException {
        Station origin = stationRepository.findById(origen).orElseThrow(()-> new BadRequestException(ORIGIN_NOT_FOUND));
        Station destination = stationRepository.findById(destiny).orElseThrow(()-> new BadRequestException(DESTINATION_NOT_FOUND));
        List<Station> stationList = stationRepository.findAll();
        List<Route> routeList = repository.findAll();

        return CalculateOptimal.getOptimalRoute(origin, destination, stationList, routeList);
    }




    /**
     *
     */
    @Override
    public void initDB() {
        // Inicializar datos de prueba en la BD
        Station a = Station.builder(). name("A").build();
        Station b = Station.builder(). name("B").build();
        Station c = Station.builder(). name("C").build();
        Station d = Station.builder(). name("D").build();

        stationRepository.saveAll(List.of(a, b, c, d));

        Route r1 = Route.builder().origin(a).destination(b).cost(5).build();
        Route r2 = Route.builder().origin(b).destination(c).cost(4).build();
        Route r3 = Route.builder().origin(c).destination(a).cost(8).build();
        Route r4 = Route.builder().origin(a).destination(d).cost(8).build();
        repository.saveAll(List.of(r1, r2, r3, r4));
    }


}
