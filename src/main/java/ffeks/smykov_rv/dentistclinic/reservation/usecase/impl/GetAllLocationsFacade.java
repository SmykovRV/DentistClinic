package ffeks.smykov_rv.dentistclinic.reservation.usecase.impl;

import ffeks.smykov_rv.dentistclinic.reservation.dto.LocationMapping;
import ffeks.smykov_rv.dentistclinic.reservation.dto.mapping.LocationDto;
import ffeks.smykov_rv.dentistclinic.reservation.model.Location;
import ffeks.smykov_rv.dentistclinic.reservation.service.LocationsService;
import ffeks.smykov_rv.dentistclinic.reservation.usecase.GetAllLocationsUseCase;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@Transactional
public class GetAllLocationsFacade implements GetAllLocationsUseCase {
    private final LocationsService locationsService;
    private final LocationMapping locationMapping;

    public GetAllLocationsFacade(LocationsService locationsService, LocationMapping locationMapping) {
        this.locationsService = locationsService;
        this.locationMapping = locationMapping;
    }

    @Override
    public List<LocationDto> getAllLocations() {
        List<Location>  locations = locationsService.getAllLocations();

//        log.info("getAllLocations: locations = {}", locations);
        List<LocationDto> allLocations = locations
                .stream()
                .map(locationMapping::toLocationDto)
                .toList();
//        log.info("getAllLocations: allLocations = {}", allLocations);
        return allLocations;
    }
}
