package ffeks.smykov_rv.dentistclinic.reservation.service;

import ffeks.smykov_rv.dentistclinic.reservation.dto.mapping.ReservationDto;
import ffeks.smykov_rv.dentistclinic.reservation.dto.mapping.TimeSlotDto;
import ffeks.smykov_rv.dentistclinic.reservation.model.Reservation;
import ffeks.smykov_rv.dentistclinic.reservation.web.model.MakeReservationRequest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ReservationService {
    List<Reservation> allReservations();
//    void addReservation(Reservation reservation);
//    boolean isPresentReservationByReservationDateAndTime(Reservation reservation);
//    boolean isPresentReservationByReservationDateTimeDoctorAndLocation(
//            LocalDate date,
//            Long locationId,
//            Long doctorId,
//            LocalTime start,
//            LocalTime end
//    );

//    void cancelReservation(long id);
    Optional<Reservation> getReservationByReservationId(long id);

    List<Reservation> getReservationsByLocationId(Long locationId);
    List<Reservation> getReservationsByDoctorId(long doctorId);

    List<Reservation> getReservationsByUsername(String username);
    void acceptReservation(Long reservationId);

    List<TimeSlotDto> getAvailableSlots(Long doctorId, LocalDate date);
    ReservationDto makeReservation(MakeReservationRequest request, Long userId);
    ReservationDto makeReservation(MakeReservationRequest request);
    void cancelReservation(Long reservationId, Long userId);
    void cancelReservationByAdministrator(Long reservationId);
}
