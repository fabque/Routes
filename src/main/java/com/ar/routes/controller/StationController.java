package com.ar.routes.controller;

import com.ar.routes.domain.model.Station;
import com.ar.routes.domain.model.dto.CreateEditStationDto;
import com.ar.routes.domain.service.StationService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
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
    public ResponseEntity<Long> createStation(@RequestBody CreateEditStationDto createStationDto) {
        Station saved = service.add(createStationDto.getName());
        return (saved.getId() == null) ? ResponseEntity.status(HttpStatus.CONFLICT).build() : ResponseEntity.status(HttpStatus.CREATED).body(saved.getId());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> putStation(@PathVariable Long id, @RequestBody CreateEditStationDto editStationDto) {
        Station saved = null;
        try {
            saved = service.edit(id, editStationDto);
        } catch (BadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(saved.getId());
    }

    /**
     * get all Stations
     * @return
     */
    @GetMapping
    public ResponseEntity<?> getStations() {
        return ResponseEntity.status(HttpStatus.OK).body(service.getAll());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStation(@PathVariable Long id) {
        try {
            service.delete(id);
            return ResponseEntity.status(HttpStatus.OK).body(id);
        } catch (BadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
