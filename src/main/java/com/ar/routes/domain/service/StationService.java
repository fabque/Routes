package com.ar.routes.domain.service;

import com.ar.routes.domain.model.Station;

import java.util.List;

public interface StationService {
    Station add(String station);
    List<Station> getAll();
}
