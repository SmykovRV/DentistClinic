package ffeks.smykov_rv.dentistclinic.reservation.usecase;

import ffeks.smykov_rv.dentistclinic.reservation.dto.mapping.ReservationDto;

import java.util.List;

public interface GetAllReservationsUseCase {
    List<ReservationDto> getAllReservations();
    List<ReservationDto> getAllReservationsForLocation();
    List<ReservationDto> getAllReservationsForDoctor();
}
