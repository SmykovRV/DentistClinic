package ffeks.smykov_rv.dentistclinic.reservation.service;

import ffeks.smykov_rv.dentistclinic.reservation.model.Administrator;

import java.util.List;

public interface AdminService {
    Administrator findAdministratorById(Long id);

    List<Administrator> findAll();
    Administrator findAdministratorByPhoneNumber(String phoneNumber);
}
