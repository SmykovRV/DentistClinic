package ffeks.smykov_rv.dentistclinic.reservation.service;

import ffeks.smykov_rv.dentistclinic.reservation.model.Doctor;
import ffeks.smykov_rv.dentistclinic.reservation.model.Location;

import java.util.List;
import java.util.Optional;

public interface LocationsService {
    Optional<Doctor> getDoctorByLocation(Long locationId, Long doctorId);
    boolean isExistById(Long locationId);
    Optional<Location> getLocationById(Long locationId);
    List<Location> getAllLocations();
}
