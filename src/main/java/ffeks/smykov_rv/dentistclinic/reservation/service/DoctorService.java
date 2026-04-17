package ffeks.smykov_rv.dentistclinic.reservation.service;

import ffeks.smykov_rv.dentistclinic.reservation.model.Doctor;

import java.util.List;

public interface DoctorService {
    Doctor findDoctorById(Long id);
    boolean existsDoctorById(Long id);
    List<Doctor> findAllDoctors();
    Doctor findDoctorByPhoneNumber(String phoneNumber);
}
