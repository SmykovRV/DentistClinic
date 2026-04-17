package ffeks.smykov_rv.dentistclinic.reservation.usecase;

public interface CancelReservationUseCase {
    void cancelReservationByAdministrator(long reservationId);

    void cancelReservationByUser(long reservationId);

}
