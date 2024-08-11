package com.ar.routes.domain.repository;

import com.ar.routes.domain.model.Route;
import com.ar.routes.domain.model.Station;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RouteRepository extends JpaRepository<Route, Long> {
    List<Route> findByOrigin(Station origin);
}
