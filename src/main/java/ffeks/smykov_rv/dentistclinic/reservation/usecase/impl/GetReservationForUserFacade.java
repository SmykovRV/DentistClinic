package ffeks.smykov_rv.dentistclinic.reservation.usecase.impl;

import ffeks.smykov_rv.dentistclinic.reservation.dto.ReservationMapping;
import ffeks.smykov_rv.dentistclinic.reservation.dto.mapping.ReservationDto;
import ffeks.smykov_rv.dentistclinic.reservation.service.ReservationService;
import ffeks.smykov_rv.dentistclinic.reservation.usecase.GetReservationForUserUseCase;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
public class GetReservationForUserFacade implements GetReservationForUserUseCase {

    private final ReservationService reservationService;
    private final ReservationMapping reservationMapping;

    public GetReservationForUserFacade(ReservationService reservationService, ReservationMapping reservationMapping) {
        this.reservationService = reservationService;
        this.reservationMapping = reservationMapping;
    }

    @Override
    public List<ReservationDto> getReservationForUser() {
        String currentUsername = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        List<ReservationDto> reservationDtos =
                reservationService
                        .getReservationsByUsername(currentUsername)
                .stream()
                .map(reservationMapping::toReservationDto)
                .toList();
        return reservationDtos;
    }
}
