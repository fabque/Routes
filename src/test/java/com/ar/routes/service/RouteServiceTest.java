package com.ar.routes.service;

import com.ar.routes.domain.model.Route;
import com.ar.routes.domain.model.Station;
import com.ar.routes.domain.model.dto.CreateRouteDto;
import com.ar.routes.domain.repository.RouteRepository;
import com.ar.routes.domain.repository.StationRepository;
import com.ar.routes.domain.service.impl.RouteServiceImpl;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RouteServiceTest {

    @Mock
    private RouteRepository repository;
    @Mock
    private StationRepository stationRepository;

    @InjectMocks
    private RouteServiceImpl service;

    @Test
    void createRouteBadOrigin() throws BadRequestException {
        when(stationRepository.findById(1)).thenReturn(Optional.ofNullable(null));

        Assertions.assertThrows(BadRequestException.class,
                () -> service.create(CreateRouteDto.builder().origin(1).destination(2).cost(10).build()));
    }

    @Test
    void createRouteBadDestination() throws BadRequestException {
        when(stationRepository.findById(1)).thenReturn(Optional.of(Station.builder().id(1).name("A").build()));
        when(stationRepository.findById(2)).thenReturn(Optional.ofNullable(null));
        Assertions.assertThrows(BadRequestException.class,
                () -> service.create(CreateRouteDto.builder().origin(1).destination(2).cost(10).build()));

    }

    @Test
    void createRouteOK() throws BadRequestException {
        Integer id = 1;
        when(stationRepository.findById(1)).thenReturn(Optional.of(Station.builder().id(1).name("A").build()));
        when(stationRepository.findById(2)).thenReturn(Optional.of(Station.builder().id(2).name("B").build()));
        when(repository.save(any(Route.class))).thenReturn(Route.builder().id(id).build());
        Assertions.assertEquals(service.create(CreateRouteDto.builder().origin(1).destination(2).cost(10).build()).getId(), id);
    }
}
