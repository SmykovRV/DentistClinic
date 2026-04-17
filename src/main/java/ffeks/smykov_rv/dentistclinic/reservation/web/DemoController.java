package ffeks.smykov_rv.dentistclinic.reservation.web;

import ffeks.smykov_rv.dentistclinic.reservation.dto.mapping.LocationDto;
import ffeks.smykov_rv.dentistclinic.reservation.dto.mapping.ReservationDto;
import ffeks.smykov_rv.dentistclinic.reservation.dto.mapping.TimeSlotDto;
import ffeks.smykov_rv.dentistclinic.reservation.service.LocationsService;
import ffeks.smykov_rv.dentistclinic.reservation.service.ReservationService;
import ffeks.smykov_rv.dentistclinic.reservation.usecase.*;
import ffeks.smykov_rv.dentistclinic.reservation.web.model.MakeReservationRequest;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/reservation")
public class DemoController {

    private final MakeReservationUseCase createReservationUseCase;
    private final GetAllReservationsUseCase getAllReservationsUseCase;
    private final GetAllLocationsUseCase getAllLocationsUseCase;
    private final CancelReservationUseCase cancelReservationUseCase;
    private final GetReservationForUserUseCase getReservationForUserUseCase;
    private final ReservationService reservationService;

    public DemoController(MakeReservationUseCase createReservation, GetAllReservationsUseCase getAllReservations, GetAllLocationsUseCase getAllLocationsUseCase, LocationsService locationsService, ReservationService reservationService, CancelReservationUseCase cancelReservationUseCase, GetReservationForUserUseCase getReservationForUserUseCase, ReservationService reservationService1) {

        this.createReservationUseCase = createReservation;
        this.getAllReservationsUseCase = getAllReservations;
        this.getAllLocationsUseCase = getAllLocationsUseCase;
        this.cancelReservationUseCase = cancelReservationUseCase;
        this.getReservationForUserUseCase = getReservationForUserUseCase;
        this.reservationService = reservationService1;
    }


    @CrossOrigin(origins = "http://localhost:5173")
    @PostMapping("/make_reservation")
    @ResponseStatus(HttpStatus.CREATED)
    public void makeReservation(@Valid @RequestBody MakeReservationRequest makeReservationRequest) {
        createReservationUseCase.makeReservation(makeReservationRequest);
    }

    @PostMapping("/cancel_reservation/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void cancelReservation(@PathVariable long id) {
        cancelReservationUseCase.cancelReservationByUser(id);
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping("/get_all_locations")
    @ResponseStatus(HttpStatus.OK)
    public List<LocationDto> getAllLocations() {
        return getAllLocationsUseCase.getAllLocations();
    }

    @GetMapping("get_reservation_for_user")
    @ResponseStatus(HttpStatus.OK)
    public List<ReservationDto> getReservationForUser() {
        return getReservationForUserUseCase.getReservationForUser();
    }
    @GetMapping("/available-slots")
    @ResponseStatus(HttpStatus.OK)
    public List<TimeSlotDto> getAvailableSlots(
            @RequestParam Long doctorId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        return reservationService.getAvailableSlots(doctorId, date);
    }








//    @GetMapping("/get_all_locations")
//    @ResponseStatus(HttpStatus.OK)
//    public List<LocationDto> getAllLocations() {
//        return getAllLocationsUseCase.getAllLocations();
//
////        return locationsService.getAllLocations();
//    }

//    @GetMapping("/all_reservations")
//    public List<ReservationDto> allReservations() {
//        return getAllReservationsUseCase.getAllReservations();
//    }

    @GetMapping("/basic-auth")
    public String basicAuth() {
        return "Basic Auth";
    }

    @GetMapping("/user-auth")
    public String userAuth() {
        return "User Auth";
    }

    @GetMapping("/admin-auth")
    public String adminAuth() {
        return "Admin Auth";
    }

    @GetMapping("/super-admin-auth")
    public String superAdminAuth() {
        return "Super admin Auth";
    }


}
