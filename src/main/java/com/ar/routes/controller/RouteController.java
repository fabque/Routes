package com.ar.routes.controller;

import com.ar.routes.domain.model.Route;
import com.ar.routes.domain.model.dto.CreateRouteDto;
import com.ar.routes.domain.service.RouteService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/routes")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RouteController {
    private final RouteService service;

    @PostMapping
    public ResponseEntity<?> createRoute(@RequestBody CreateRouteDto createRouteDto) {
        Route saved = null;
        try {
            saved = service.create(createRouteDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved.getId());
        } catch (BadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping
    public List<Route> getRoutes() {
        return service.getRoutes();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRoutes(@PathVariable Integer id) {
        try {
            service.deleteRoute(id);
           return ResponseEntity.status(HttpStatus.OK).body(id);
        } catch (BadRequestException e) {
           return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    /**
     *
     * @param origin Id of entity Station
     * @param destiny Id of entity Station
     * @return
     */
    @GetMapping("/optimal")
    public ResponseEntity<?> getOptimalRoute(@RequestParam Integer origin, @RequestParam Integer destiny) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(service.getOptimalRoute(origin, destiny));
        } catch (BadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    /**
     * Method to iniziate a basic routes with 4 stations named A, B, C and D
     * The four stations are interconnected
     * @return
     */
    @GetMapping("/initdb")
    public ResponseEntity<?> initDatabaseRoutes() {
        service.initDB();
        return ResponseEntity.status(HttpStatus.OK).body("Iniciado");
    }

}
