package com.ar.routes.domain.service;

import com.ar.routes.domain.model.Route;
import com.ar.routes.domain.model.Station;
import com.ar.routes.domain.model.dto.CreateRouteDto;
import com.ar.routes.domain.model.dto.RouteResponse;
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

import java.util.ArrayList;
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

    private static final String  nameA = "A";
    private static final String  nameB = "B";
    private static final Long idA = 1L;
    private static final Long idB = 2L;
    private static final Long routeId = 1L;
    Station stationA = Station.builder().id(idA).name(nameA).build();
    Station stationB = Station.builder().id(idA).name(nameB).build();
    Route route = new Route(routeId, stationA, stationB, 5);


    /**
     * Tests Create Route
     * @throws BadRequestException
     */
    @Test
    void createRoute_SameOriginDestination() throws BadRequestException {
        Assertions.assertThrows(BadRequestException.class,
                () -> service.create(CreateRouteDto.builder().origin(idA).destination(idA).cost(10).build()));
    }

    @Test
    void testCreateRoute_BadOrigin() throws BadRequestException {
        when(stationRepository.findById(idA)).thenReturn(Optional.ofNullable(null));

        Assertions.assertThrows(BadRequestException.class,
                () -> service.create(CreateRouteDto.builder().origin(idA).destination(idB).cost(10).build()));
    }

    @Test
    void testCreateRoute_BadDestination() throws BadRequestException {
        when(stationRepository.findById(idA)).thenReturn(Optional.of(stationA));
        when(stationRepository.findById(idB)).thenReturn(Optional.ofNullable(null));
        Assertions.assertThrows(BadRequestException.class,
                () -> service.create(CreateRouteDto.builder().origin(idA).destination(idB).cost(10).build()));

    }

    @Test
    void testCreateRoute_OK() throws BadRequestException {

        when(stationRepository.findById(idA)).thenReturn(Optional.of(stationA));
        when(stationRepository.findById(idB)).thenReturn(Optional.of(stationB));
        when(repository.save(any(Route.class))).thenReturn(Route.builder().id(routeId).build());
        Assertions.assertEquals(service.create(CreateRouteDto.builder().origin(idA).destination(idB).cost(10).build()).getId(), routeId);
    }

    /**
     * test Edit Route
     * @throws BadRequestException
     */
    @Test
    public void testEditRoute_Success() throws BadRequestException {
        CreateRouteDto editDto = new CreateRouteDto(idA, idB, 50);
        Route existingRoute = new Route(routeId, stationA, stationB, 10);


        when(repository.findById(routeId)).thenReturn(Optional.of(existingRoute));
        when(stationRepository.findById(editDto.getOrigin())).thenReturn(Optional.of(stationA));
        when(stationRepository.findById(editDto.getDestination())).thenReturn(Optional.of(stationB));
        when(repository.save(existingRoute)).thenReturn(existingRoute);

        Route result = service.edit(routeId, editDto);

        verify(repository).save(existingRoute);
        Assertions.assertEquals(50, result.getCost());
        Assertions.assertEquals(stationA, result.getOrigin());
        Assertions.assertEquals(stationB, result.getDestination());
    }

    @Test
    public void testEditRoute_NotFound() {
        Long routeId = 1L;
        CreateRouteDto editDto = new CreateRouteDto(1L, 2L, 50);

        when(repository.findById(routeId)).thenReturn(Optional.empty());

        Assertions.assertThrows(BadRequestException.class, () -> {
            service.edit(routeId, editDto);
        });
    }

    @Test
    public void testEditRoute_OriginNotFound() {
        Long routeId = 1L;
        CreateRouteDto editDto = new CreateRouteDto(1L, 2L, 50);
        Route existingRoute = new Route(routeId, new Station(1L, "A"), new Station(2L, "B"), 10);

        when(repository.findById(routeId)).thenReturn(Optional.of(existingRoute));
        when(stationRepository.findById(editDto.getOrigin())).thenReturn(Optional.empty());

        Assertions.assertThrows(BadRequestException.class, () -> {
            service.edit(routeId, editDto);
        });
    }

    @Test
    public void testEditRoute_DestinationNotFound() {
        Long routeId = 1L;
        CreateRouteDto editDto = new CreateRouteDto(1L, 2L, 50);
        Route existingRoute = new Route(routeId, new Station(1L, "A"), new Station(2L, "B"), 10);
        Station origin = new Station(1L, "A");

        when(repository.findById(routeId)).thenReturn(Optional.of(existingRoute));
        when(stationRepository.findById(editDto.getOrigin())).thenReturn(Optional.of(origin));
        when(stationRepository.findById(editDto.getDestination())).thenReturn(Optional.empty());

        Assertions.assertThrows(BadRequestException.class, () -> {
            service.edit(routeId, editDto);
        });
    }

    /**
     * tests getRoutes
     */
    @Test
    void testGetRoutes_Empty(){
        when(repository.findAll()).thenReturn(new ArrayList<>());
        Assertions.assertTrue(service.getRoutes().isEmpty());
    }

    @Test
    void testGetRoutes_List(){
        List<Route> routeList = new ArrayList<>();
        routeList.add(Route.builder().origin(stationA).destination(stationB).cost(5).build());

        when(repository.findAll()).thenReturn(routeList);
        Assertions.assertEquals(service.getRoutes().size(), routeList.size());
    }

    /**
     * test Delete route
     * @throws BadRequestException
     */
    @Test
    public void testDeleteRoute_Success() throws BadRequestException {
        when(repository.findById(routeId)).thenReturn(Optional.of(route));

        service.deleteRoute(routeId);
        verify(repository).delete(route);
    }

    @Test
    public void testDeleteRoute_NotFound() {
        when(repository.findById(routeId)).thenReturn(Optional.empty());

        BadRequestException exception = Assertions.assertThrows(BadRequestException.class, () -> {
            service.deleteRoute(routeId);
        });
        Assertions.assertEquals("Not Found", exception.getMessage());
    }

    /**
     * Test getOptimalRoute
     * @throws BadRequestException
     */
    @Test
    public void testGetOptimalRoute_Success() throws BadRequestException {
        List<Station> stationList = List.of(stationA, stationB);
        List<Route> routeList = List.of(new Route(routeId, stationA, stationB, 5));
        List<Long> expectedPath = List.of(stationA.getId(), stationB.getId());
        RouteResponse expectedRouteResponse = RouteResponse.builder().path(expectedPath).cost(5).build();

        when(stationRepository.findById(idA)).thenReturn(Optional.of(stationA));
        when(stationRepository.findById(idB)).thenReturn(Optional.of(stationB));
        when(stationRepository.findAll()).thenReturn(stationList);
        when(repository.findAll()).thenReturn(routeList);

        try (MockedStatic<CalculateOptimal> mockedCalculateOptimal = mockStatic(CalculateOptimal.class)) {
            mockedCalculateOptimal.when(() -> CalculateOptimal.getOptimalRoute(stationA, stationB, stationList, routeList))
                    .thenReturn(expectedRouteResponse);

            RouteResponse result = service.getOptimalRoute(idA, idB);

            Assertions.assertEquals(expectedRouteResponse, result);
        }
    }

    @Test
    public void testGetOptimalRoute_OriginNotFound() {
        when(stationRepository.findById(idA)).thenReturn(Optional.empty());

        BadRequestException exception = Assertions.assertThrows(BadRequestException.class, () -> {
            service.getOptimalRoute(idA, idB);
        });

        Assertions.assertEquals("Origin Not Found", exception.getMessage());
    }

    @Test
    public void testGetOptimalRoute_DestinationNotFound() {
        Station origin = new Station(idA, "A");

        when(stationRepository.findById(idA)).thenReturn(Optional.of(origin));
        when(stationRepository.findById(idB)).thenReturn(Optional.empty());

        BadRequestException exception = Assertions.assertThrows(BadRequestException.class, () -> {
            service.getOptimalRoute(idA, idB);
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
