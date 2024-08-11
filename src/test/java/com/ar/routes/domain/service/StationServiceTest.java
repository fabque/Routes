package com.ar.routes.domain.service;

import com.ar.routes.domain.model.Station;
import com.ar.routes.domain.model.dto.CreateEditStationDto;
import com.ar.routes.domain.repository.StationRepository;
import com.ar.routes.domain.service.impl.StationServiceImpl;
import org.apache.coyote.BadRequestException;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class StationServiceTest {

    @Mock
    private StationRepository repository;

    @InjectMocks
    private StationServiceImpl service;

    private static final String  nameA = "A";
    private static final String  nameB = "B";
    private static final Long idA = 1L;
    private static final Long idB = 2L;

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

        Station newStation = Station.builder().id(idA).name(name).build();
        when(repository.findStationByName(name))
                .thenReturn(Optional.ofNullable(null));
        when(repository.save(any())).thenReturn(newStation);
        Assertions.assertEquals(service.add(name).getId(), idA);
    }

    @Test
    public void testEditStation_Success() throws BadRequestException {
        Long stationId = 1L;
        CreateEditStationDto editDto = new CreateEditStationDto("NewName");
        Station existingStation = new Station(stationId, "OldName");

        when(repository.findById(stationId)).thenReturn(Optional.of(existingStation));
        when(repository.findStationByName(editDto.getName())).thenReturn(Optional.empty());

        Station result = service.edit(stationId, editDto);

        verify(repository).save(existingStation);
        Assertions.assertEquals("NewName", result.getName());
    }

    @Test
    public void testEditStation_DuplicateName() {
        Long stationId = 1L;
        CreateEditStationDto editDto = new CreateEditStationDto("ExistingName");
        Station existingStation = new Station(stationId, "OldName");
        Station anotherStation = new Station(2L, "ExistingName");

        when(repository.findById(stationId)).thenReturn(Optional.of(existingStation));
        when(repository.findStationByName(editDto.getName())).thenReturn(Optional.of(anotherStation));

        Assertions.assertThrows(BadRequestException.class, () -> {
            service.edit(stationId, editDto);
        });
    }

    @Test
    public void testEditStation_NotFound() {
        Long stationId = 1L;
        CreateEditStationDto editDto = new CreateEditStationDto("NewName");

        when(repository.findById(stationId)).thenReturn(Optional.empty());

        Assertions.assertThrows(BadRequestException.class, () -> {
            service.edit(stationId, editDto);
        });
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

    /**
     * test Delete station
     * @throws BadRequestException
     */
    @Test
    public void testDeleteStation_Success() throws BadRequestException {
        when(repository.findById(idA)).thenReturn(Optional.of(stationA));

        service.delete(idA);
        verify(repository).delete(stationA);
    }

    @Test
    public void testDeleteRoute_NotFound() {
        when(repository.findById(idA)).thenReturn(Optional.empty());

        BadRequestException exception = Assertions.assertThrows(BadRequestException.class, () -> {
            service.delete(idA);
        });
        Assertions.assertEquals("Not Found", exception.getMessage());
    }
}
