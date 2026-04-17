package ffeks.smykov_rv.dentistclinic.reservation.usecase.impl;

import ffeks.smykov_rv.dentistclinic.reservation.service.ReservationService;
import ffeks.smykov_rv.dentistclinic.reservation.usecase.CancelReservationUseCase;
import ffeks.smykov_rv.dentistclinic.security.service.UserAccountService;
import jakarta.transaction.Transactional;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@Transactional
public class CancelReservationFacade implements CancelReservationUseCase {

    private final ReservationService reservationService;
    private final UserAccountService userAccountService;

    public CancelReservationFacade(ReservationService reservationService, UserAccountService userAccountService) {
        this.reservationService = reservationService;
        this.userAccountService = userAccountService;
    }

    @Override
    public void cancelReservationByAdministrator(long reservationId) {
        reservationService.cancelReservationByAdministrator(reservationId);
    }

    @Override
    public void cancelReservationByUser(long reservationId) {
        Long userId = userAccountService.getUserByPhone(SecurityContextHolder.getContext().getAuthentication().getName()).get().getId();
        reservationService.cancelReservation(reservationId, userId);
    }
}
