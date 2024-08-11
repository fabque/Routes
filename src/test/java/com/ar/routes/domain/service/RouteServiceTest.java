package com.ar.routes.domain.service;

import com.ar.routes.domain.model.Route;
import com.ar.routes.domain.model.Station;
import com.ar.routes.domain.model.dto.CreateRouteDto;
import com.ar.routes.domain.repository.RouteRepository;
import com.ar.routes.domain.repository.StationRepository;
import com.ar.routes.domain.service.impl.RouteServiceImpl;
import com.ar.routes.domain.service.utils.CalculateOptimal;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RouteServiceTest {

    @Mock
    private RouteRepository repository;
    @Mock
    private StationRepository stationRepository;

    @InjectMocks
    private RouteServiceImpl service;

    /**
     * Tests Create Route
     * @throws BadRequestException
     */
    @Test
    void createRoute_SameOriginDestination() throws BadRequestException {
        Assertions.assertThrows(BadRequestException.class,
                () -> service.create(CreateRouteDto.builder().origin(1).destination(1).cost(10).build()));
    }

    @Test
    void testCreateRoute_BadOrigin() throws BadRequestException {
        when(stationRepository.findById(1)).thenReturn(Optional.ofNullable(null));

        Assertions.assertThrows(BadRequestException.class,
                () -> service.create(CreateRouteDto.builder().origin(1).destination(2).cost(10).build()));
    }

    @Test
    void testCreateRoute_BadDestination() throws BadRequestException {
        when(stationRepository.findById(1)).thenReturn(Optional.of(Station.builder().id(1).name("A").build()));
        when(stationRepository.findById(2)).thenReturn(Optional.ofNullable(null));
        Assertions.assertThrows(BadRequestException.class,
                () -> service.create(CreateRouteDto.builder().origin(1).destination(2).cost(10).build()));

    }

    @Test
    void testCreateRoute_OK() throws BadRequestException {
        Integer id = 1;
        when(stationRepository.findById(1)).thenReturn(Optional.of(Station.builder().id(1).name("A").build()));
        when(stationRepository.findById(2)).thenReturn(Optional.of(Station.builder().id(2).name("B").build()));
        when(repository.save(any(Route.class))).thenReturn(Route.builder().id(id).build());
        Assertions.assertEquals(service.create(CreateRouteDto.builder().origin(1).destination(2).cost(10).build()).getId(), id);
    }


    /**
     * Test getOptimalRoute
     * @throws BadRequestException
     */
    @Test
    public void testGetOptimalRoute_Success() throws BadRequestException {
        Station origin = new Station(1, "A");
        Station destination = new Station(2, "B");
        List<Station> stationList = List.of(origin, destination);
        List<Route> routeList = List.of(new Route(1, origin, destination, 5));
        List<Station> expectedRoute = List.of(origin, destination);

        when(stationRepository.findById(1)).thenReturn(Optional.of(origin));
        when(stationRepository.findById(2)).thenReturn(Optional.of(destination));
        when(stationRepository.findAll()).thenReturn(stationList);
        when(repository.findAll()).thenReturn(routeList);

        try (MockedStatic<CalculateOptimal> mockedCalculateOptimal = mockStatic(CalculateOptimal.class)) {
            mockedCalculateOptimal.when(() -> CalculateOptimal.getOptimalRoute(origin, destination, stationList, routeList))
                    .thenReturn(expectedRoute);

            List<Station> result = service.getOptimalRoute(1, 2);

            Assertions.assertEquals(expectedRoute, result);
        }
    }

    @Test
    public void testGetOptimalRoute_OriginNotFound() {
        when(stationRepository.findById(1)).thenReturn(Optional.empty());

        BadRequestException exception = Assertions.assertThrows(BadRequestException.class, () -> {
            service.getOptimalRoute(1, 2);
        });

        Assertions.assertEquals("Origin Not Found", exception.getMessage());
    }

    @Test
    public void testGetOptimalRoute_DestinationNotFound() {
        Station origin = new Station(1, "A");

        when(stationRepository.findById(1)).thenReturn(Optional.of(origin));
        when(stationRepository.findById(2)).thenReturn(Optional.empty());

        BadRequestException exception = Assertions.assertThrows(BadRequestException.class, () -> {
            service.getOptimalRoute(1, 2);
        });

        Assertions.assertEquals("Destination Not Found", exception.getMessage());
    }



    /**
     * Test init database stations and routes
     */
    @Test
    public void testInitDB() {
        service.initDB();

        verify(stationRepository).saveAll(anyList());
        verify(repository).saveAll(anyList());
    }
}
