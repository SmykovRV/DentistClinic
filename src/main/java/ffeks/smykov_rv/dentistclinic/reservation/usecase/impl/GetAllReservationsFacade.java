package ffeks.smykov_rv.dentistclinic.reservation.usecase.impl;

import ffeks.smykov_rv.dentistclinic.reservation.dto.DoctorMapping;
import ffeks.smykov_rv.dentistclinic.reservation.dto.ReservationMapping;
import ffeks.smykov_rv.dentistclinic.reservation.dto.UserAccountMapper;
import ffeks.smykov_rv.dentistclinic.reservation.dto.mapping.ReservationDto;
import ffeks.smykov_rv.dentistclinic.reservation.model.Administrator;
import ffeks.smykov_rv.dentistclinic.reservation.model.Doctor;
import ffeks.smykov_rv.dentistclinic.reservation.service.AdminService;
import ffeks.smykov_rv.dentistclinic.reservation.service.DoctorService;
import ffeks.smykov_rv.dentistclinic.reservation.service.ReservationService;
import ffeks.smykov_rv.dentistclinic.reservation.usecase.GetAllReservationsUseCase;
import jakarta.transaction.Transactional;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
@Transactional
public class GetAllReservationsFacade implements GetAllReservationsUseCase {

    private final ReservationMapping reservationMapping;
    private final ReservationService reservationService;
    private final AdminService adminService;
    private final DoctorService doctorService;

    public GetAllReservationsFacade(ReservationMapping reservationMapping, ReservationService reservationService, DoctorMapping doctorMapping, UserAccountMapper userAccountMapper, DoctorMapping doctorMapping1, UserAccountMapper userAccountMapper1, AdminService adminService, DoctorService doctorService) {
        this.reservationMapping = reservationMapping;
        this.reservationService = reservationService;
        this.adminService = adminService;
        this.doctorService = doctorService;
    }

    @Override
    public List<ReservationDto> getAllReservations() {

        List<ReservationDto> dtos = reservationService
                .allReservations()
                .stream()
                .map(reservationMapping::toReservationDto)
                .toList();
        return dtos;
    }

    @Override
    public List<ReservationDto> getAllReservationsForLocation() {
        Administrator administrator = adminService.findAdministratorByPhoneNumber(
                Objects.requireNonNull(
                        SecurityContextHolder.getContext().getAuthentication()).getName());

        List<ReservationDto> dtos = reservationService
                .getReservationsByLocationId(administrator.getLocation().getId())
                .stream()
                .map(reservationMapping::toReservationDto)
                .toList();
        return dtos;
    }

    @Override
    public List<ReservationDto> getAllReservationsForDoctor() {

        Doctor doctor = doctorService.findDoctorByPhoneNumber(
                Objects.requireNonNull(
                        SecurityContextHolder.getContext().getAuthentication()).getName());
        List<ReservationDto> dtos = reservationService
                .getReservationsByDoctorId(doctor.getId())
                .stream()
                .map(reservationMapping::toReservationDto)
                .toList();
        return dtos;
    }
}
