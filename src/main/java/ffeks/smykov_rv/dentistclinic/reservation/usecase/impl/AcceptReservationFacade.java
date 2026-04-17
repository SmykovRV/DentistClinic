package ffeks.smykov_rv.dentistclinic.reservation.usecase.impl;

import ffeks.smykov_rv.dentistclinic.reservation.model.Reservation;
import ffeks.smykov_rv.dentistclinic.reservation.service.ReservationService;
import ffeks.smykov_rv.dentistclinic.reservation.usecase.AcceptReservationUseCase;
import jakarta.transaction.Transactional;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@Component
@Transactional
public class AcceptReservationFacade implements AcceptReservationUseCase {

    private final ReservationService reservationService;

    public AcceptReservationFacade(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @Override
    public void acceptReservation(Long reservationId) {
        Optional<Reservation> r = reservationService.getReservationByReservationId(reservationId);

        if (r.isPresent()) {
            Reservation reservation = r.get();
            if (!reservation.isCanceled()){
                if (!reservation.isAccepted()){
                    String currentUsername = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
                    reservation
                            .getLocation()
                            .getAdministrators()
                            .forEach(
                                    (administrator) -> {
                                        if (currentUsername.equals(administrator.getUserAccount().getUsername())) {
                                            reservationService.acceptReservation(reservationId);
                                        }
                            });
                }
                else {
                    throw new RuntimeException("Reservation already accepted");
                }
            }
            else  {
                throw new RuntimeException("Reservation already cancelled");
            }
        }
        else {
            throw new RuntimeException("Reservation not found");
        }
    }
}
