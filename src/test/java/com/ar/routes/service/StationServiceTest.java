package com.ar.routes.service;

import com.ar.routes.domain.model.Station;
import com.ar.routes.domain.repository.StationRepository;
import com.ar.routes.domain.service.StationService;
import com.ar.routes.domain.service.impl.StationServiceImpl;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class StationServiceTest {

    @Mock
    private StationRepository repository;

    @InjectMocks
    private StationServiceImpl service;

    private static final String  nameA = "A";
    private static final String  nameB = "B";
    private static final Integer idA = 1;
    private static final Integer idB = 2;

    private Station newStation;
    private Station stationA;
    private Station stationB;


    @BeforeEach
    void setUp() {
        newStation = Station.builder().name(nameA).build();
        stationA = Station.builder().id(idA).name(nameA).build();
        stationB = Station.builder().id(idB).name(nameB).build();
    }

    @Test
    void addExistingStation() {

        when(repository.findStationByName(nameA))
                .thenReturn(Optional.ofNullable(newStation));
        Assertions.assertNull(service.add(nameA).getId());
    }

    @Test
    void addStationSuccess() {
        String name = "A";
        Integer id = 1;
        Station newStation = Station.builder().id(id).name(name).build();
        when(repository.findStationByName(name))
                .thenReturn(Optional.ofNullable(null));
        when(repository.save(any())).thenReturn(newStation);
        Assertions.assertEquals(service.add(name).getId(), id);
    }

    @Test
    void getStationsEmpty() {
        when(repository.findAll()).thenReturn(new ArrayList<>());
        Assertions.assertTrue(service.getAll().isEmpty());
    }

    @Test
    void getAllStations() {
        List<Station> stationList = new ArrayList<>();
        stationList.add(stationA);
        stationList.add(stationB);
        when(repository.findAll()).thenReturn(stationList);
        Assertions.assertEquals(service.getAll().size(), stationList.size());
    }
}
