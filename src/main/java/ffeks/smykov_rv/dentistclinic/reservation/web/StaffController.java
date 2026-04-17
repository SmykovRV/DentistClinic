package ffeks.smykov_rv.dentistclinic.reservation.web;

import ffeks.smykov_rv.dentistclinic.reservation.dto.mapping.ReservationDto;
import ffeks.smykov_rv.dentistclinic.reservation.usecase.AcceptReservationUseCase;
import ffeks.smykov_rv.dentistclinic.reservation.usecase.CancelReservationUseCase;
import ffeks.smykov_rv.dentistclinic.reservation.usecase.GetAllReservationsUseCase;
import ffeks.smykov_rv.dentistclinic.reservation.usecase.MakeReservationUseCase;
import ffeks.smykov_rv.dentistclinic.reservation.web.model.MakeReservationRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/staff")
public class StaffController {

    private final CancelReservationUseCase cancelReservationUseCase;
    private final GetAllReservationsUseCase getAllReservationsUseCase;
    private final AcceptReservationUseCase acceptReservationUseCase;
    private final MakeReservationUseCase makeReservationUseCase;

    public StaffController(CancelReservationUseCase cancelReservationUseCase, GetAllReservationsUseCase getAllReservationsUseCase, AcceptReservationUseCase acceptReservationUseCase, MakeReservationUseCase makeReservationUseCase) {
        this.cancelReservationUseCase = cancelReservationUseCase;
        this.getAllReservationsUseCase = getAllReservationsUseCase;
        this.acceptReservationUseCase = acceptReservationUseCase;
        this.makeReservationUseCase = makeReservationUseCase;
    }

    @PostMapping("/accept_reservation/{reservationId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void acceptReservation(@PathVariable("reservationId") Long reservationId) {
        acceptReservationUseCase.acceptReservation(reservationId);
    }


    @PostMapping("/cancel_reservation/{reservationId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void cancelReservationByAdministrator(@PathVariable Long reservationId) {
        cancelReservationUseCase.cancelReservationByAdministrator(reservationId);
    }

    @GetMapping("/get_all_reservations_for_location")
    @ResponseStatus(HttpStatus.OK)
    public List<ReservationDto> getAllReservationsForLocation() {
        return getAllReservationsUseCase.getAllReservationsForLocation();
    }

    @GetMapping("/get_all_reservations_for_doctor")
    @ResponseStatus(HttpStatus.OK)
    public List<ReservationDto> getAllReservationsForDoctor() {
        return getAllReservationsUseCase.getAllReservationsForDoctor();
    }

    @PostMapping("/make_reservation")
    @ResponseStatus(HttpStatus.CREATED)
    public void makeReservationByAdministrator(@RequestBody MakeReservationRequest makeReservationRequest) {
        makeReservationUseCase.makeReservationByAdministrator(makeReservationRequest);
    }
}
