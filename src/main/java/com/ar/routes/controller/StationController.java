package com.ar.routes.controller;

import com.ar.routes.domain.model.Station;
import com.ar.routes.domain.model.dto.CreateStationDto;
import com.ar.routes.domain.service.StationService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/stations")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StationController {

    private final StationService service;

    /**
     *
     * @param createStationDto
     * @return
     */
    @PostMapping
    public ResponseEntity<Integer> createStation(@RequestBody CreateStationDto createStationDto) {
        Station saved = service.add(createStationDto.getName());
        return (saved.getId() == null) ? ResponseEntity.status(HttpStatus.CONFLICT).build() : ResponseEntity.status(HttpStatus.CREATED).body(saved.getId());
    }

    @GetMapping
    public ResponseEntity<?> getStations() {
        return ResponseEntity.status(HttpStatus.OK).body(service.getAll());
    }
}
