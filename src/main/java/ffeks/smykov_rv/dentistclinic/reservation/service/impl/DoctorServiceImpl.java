package ffeks.smykov_rv.dentistclinic.reservation.service.impl;

import ffeks.smykov_rv.dentistclinic.reservation.model.Doctor;
import ffeks.smykov_rv.dentistclinic.reservation.repository.DoctorRepository;
import ffeks.smykov_rv.dentistclinic.reservation.service.DoctorService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;

    public DoctorServiceImpl(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    @Override
    public Doctor findDoctorById(Long id) {
        if (doctorRepository.findDoctorById(id).isPresent()) {
            return doctorRepository.findDoctorById(id).get();
        }
        else{
            throw new RuntimeException("Doctor not found");
        }
    }

    @Override
    public boolean existsDoctorById(Long id) {
        if (doctorRepository.findDoctorById(id).isPresent()) {
            return true;
        }
        else{
            throw new RuntimeException("Doctor not found");
        }
    }

    @Override
    public List<Doctor> findAllDoctors() {
        return doctorRepository.findAll();
    }

    @Override
    public Doctor findDoctorByPhoneNumber(String phoneNumber) {
        Optional<Doctor> doctor = doctorRepository.getDoctorByUserAccountPhoneNumber(phoneNumber);
        if (doctor.isPresent()) {
            return doctor.get();
        }
        else {
            throw new RuntimeException("Doctor not found");
        }
    }
}
