package ffeks.smykov_rv.dentistclinic.reservation.usecase;

import ffeks.smykov_rv.dentistclinic.reservation.dto.mapping.ReservationDto;

import java.util.List;

public interface GetReservationForUserUseCase {
    public List<ReservationDto> getReservationForUser();
}
