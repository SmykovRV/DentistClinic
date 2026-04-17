package ffeks.smykov_rv.dentistclinic.reservation.usecase;

import ffeks.smykov_rv.dentistclinic.reservation.web.model.MakeReservationRequest;

public interface MakeReservationUseCase {
    void makeReservation(MakeReservationRequest makeReservationRequest);
    void makeReservationByAdministrator(MakeReservationRequest makeReservationRequest);
}
