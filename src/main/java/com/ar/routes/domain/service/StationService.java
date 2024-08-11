package com.ar.routes.domain.service;

import com.ar.routes.domain.model.Station;
import org.apache.coyote.BadRequestException;

import java.util.List;

public interface StationService {
    Station add(String station);
    List<Station> getAll();
    void delete(Integer id) throws BadRequestException;
}
