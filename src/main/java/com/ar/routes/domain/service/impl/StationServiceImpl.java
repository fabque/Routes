package com.ar.routes.domain.service.impl;

import com.ar.routes.domain.model.Station;
import com.ar.routes.domain.model.dto.CreateEditStationDto;
import com.ar.routes.domain.repository.StationRepository;
import com.ar.routes.domain.service.StationService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StationServiceImpl implements StationService {

    private static final String NOT_FOUND = "Not Found";

    @Autowired
    private final StationRepository repository;

    @Override
    public Station add(String stationName) {
        Station newStation = Station.builder().name(stationName).build();
        if (repository.findStationByName(stationName).isPresent()){
            return newStation;
        }
        return repository.save(newStation);
    }

    @Override
    public Station edit(Long id, CreateEditStationDto editStationDto) throws BadRequestException {
        Station existing = repository.findById(id).orElseThrow(()-> new BadRequestException(NOT_FOUND));
        Optional<Station> optionalStation = repository.findStationByName(editStationDto.getName());
        if (optionalStation.isEmpty() || optionalStation.get().getId() == id) { //not exists another with same name
                existing.setName(editStationDto.getName());
                repository.save(existing);
                return existing;
        } else {
            throw new BadRequestException(NOT_FOUND);
        }
    }

    @Override
    public List<Station> getAll() {
        return repository.findAll();
    }

    @Override
    public void delete(Long id) throws BadRequestException {
        Station entity = repository.findById(id).orElseThrow(()-> new BadRequestException(NOT_FOUND));
        repository.delete(entity);
    }
}
