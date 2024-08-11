package com.ar.routes.domain.service;

import com.ar.routes.domain.model.Station;
import com.ar.routes.domain.model.dto.CreateEditStationDto;
import org.apache.coyote.BadRequestException;

import java.util.List;

public interface StationService {
    Station add(String station);
    List<Station> getAll();
    void delete(Long id) throws BadRequestException;

    Station edit(Long id, CreateEditStationDto editStationDto) throws BadRequestException;
}
