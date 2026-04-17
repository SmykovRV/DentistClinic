package ffeks.smykov_rv.dentistclinic.reservation.service.impl;

import ffeks.smykov_rv.dentistclinic.reservation.buffer.impl.ReservationBufferImpl;
import ffeks.smykov_rv.dentistclinic.reservation.dto.ReservationMapping;
import ffeks.smykov_rv.dentistclinic.reservation.dto.mapping.ReservationDto;
import ffeks.smykov_rv.dentistclinic.reservation.dto.mapping.TimeSlotDto;
import ffeks.smykov_rv.dentistclinic.reservation.model.Doctor;
import ffeks.smykov_rv.dentistclinic.reservation.model.Reservation;
import ffeks.smykov_rv.dentistclinic.reservation.repository.DoctorRepository;
import ffeks.smykov_rv.dentistclinic.reservation.repository.ReservationRepository;
import ffeks.smykov_rv.dentistclinic.reservation.service.DoctorService;
import ffeks.smykov_rv.dentistclinic.reservation.service.ReservationService;
import ffeks.smykov_rv.dentistclinic.reservation.web.model.MakeReservationRequest;
import ffeks.smykov_rv.dentistclinic.security.model.UserAccount;
import ffeks.smykov_rv.dentistclinic.security.service.UserAccountService;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationBufferImpl reservationBuffer;
    private final DoctorService doctorService;
    private final UserAccountService userAccountService;
    private final ReservationMapping reservationMapping;

    public ReservationServiceImpl(ReservationRepository reservationRepository, ReservationBufferImpl reservationBuffer, DoctorRepository doctorRepository, DoctorService doctorService, UserAccountService userAccountService, ReservationMapping reservationMapping) {
        this.reservationRepository = reservationRepository;
        this.reservationBuffer = reservationBuffer;
        this.doctorService = doctorService;
        this.userAccountService = userAccountService;
        this.reservationMapping = reservationMapping;
    }

    @Override
    public List<Reservation> allReservations() {
        return reservationRepository.findAll();
    }

//    @Override
//    public void addReservation(Reservation reservation) {
//        reservationRepository.save(reservation);
//    }
//
//    @Override
//    public boolean isPresentReservationByReservationDateTimeDoctorAndLocation(
//            LocalDate date,
//            Long locationId,
//            Long doctorId,
//            LocalTime start,
//            LocalTime end)
//    {
//        Optional<Reservation> res = reservationRepository.findReservationByReservationDateTimeDoctorAndLocation(
//                date, locationId, doctorId, start, end
//        );
//        return res.isPresent();
//    }
//
//    @Override
//    public void cancelReservation(long reservationId) {
//        reservationRepository.cancelReservationByAdmin(reservationId);
//    }

    @Override
    public Optional<Reservation> getReservationByReservationId(long id) {
        return reservationRepository.getReservationsById(id);
    }

    @Override
    public List<Reservation> getReservationsByLocationId(Long locationId) {
        return reservationRepository.getByLocationId(locationId);
    }

    @Override
    public List<Reservation> getReservationsByDoctorId(long doctorId) {
        return reservationRepository.getReservationsByDoctorId(doctorId);
    }

    @Override
    public List<Reservation> getReservationsByUsername(String username) {
        return reservationRepository.getReservationsByUserAccount(username);
    }

    @Override
    public void acceptReservation(Long reservationId) {
        reservationRepository.acceptReservation(reservationId);
    }

    @Override
    public List<TimeSlotDto> getAvailableSlots(Long doctorId, LocalDate date) {
        if (date.getDayOfWeek() == DayOfWeek.SUNDAY) {
            throw new IllegalArgumentException("Клініка не працює в неділю");
        }
        if (date.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Не можна обрати минулу дату");
        }

        return reservationBuffer.getAvailableSlots(doctorId, date)
                .stream()
                .map(slot -> new TimeSlotDto(slot.getStartTime(), slot.getEndTime()))
                .toList();
    }

    @Override
    public ReservationDto makeReservation(MakeReservationRequest request, Long userId) {
        tryBook(request);

        try {
            Doctor doctor = doctorService.findDoctorById(request.doctorId());
            UserAccount user = userAccountService.getUserById(userId);

            Reservation reservation = new Reservation();
            reservation.setDoctor(doctor);
            reservation.setLocation(doctor.getLocation()); // локація береться з лікаря
            reservation.setUserAccount(user);
            reservation.setReservationDate(request.date());
            reservation.setStartTime(request.startTime());
            reservation.setEndTime(request.startTime().plusMinutes(request.serviceDurationMinutes()));
            reservation.setReservationDescription(request.description());
            reservation.setAccepted(false);
            reservation.setCanceled(false);

            Reservation saved = reservationRepository.save(reservation);
            return reservationMapping.toReservationDto(saved); // ← ось так

        } catch (Exception e) {
            reservationBuffer.releaseSlot(request.doctorId(), request.date(), request.startTime(), request.serviceDurationMinutes());
            throw e;
        }
    }

    @Override
    public ReservationDto makeReservation(MakeReservationRequest request) {
        tryBook(request);

        try {
            Doctor doctor = doctorService.findDoctorById(request.doctorId());

            Reservation reservation = new Reservation();
            reservation.setDoctor(doctor);
            reservation.setLocation(doctor.getLocation()); // локація береться з лікаря
            reservation.setUserAccount(null);
            reservation.setReservationDate(request.date());
            reservation.setStartTime(request.startTime());
            reservation.setEndTime(request.startTime().plusMinutes(request.serviceDurationMinutes()));
            reservation.setReservationDescription(request.description());
            reservation.setAccepted(false);
            reservation.setCanceled(false);

            Reservation saved = reservationRepository.save(reservation);
            return reservationMapping.toReservationDto(saved); // ← ось так

        } catch (Exception e) {
            reservationBuffer.releaseSlot(request.doctorId(), request.date(), request.startTime(), request.serviceDurationMinutes());
            throw e;
        }
    }

    @Override
    public void cancelReservation(Long reservationId, Long userId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new EntityNotFoundException("Бронювання не знайдено"));

        if (!reservation.getUserAccount().getId().equals(userId)) {
            throw new AccessDeniedException("Це не ваше бронювання");
        }

        cancelReservationAndClearBuffer(reservation);

    }

    @Override
    public void cancelReservationByAdministrator(Long reservationId) {

        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new EntityNotFoundException("Бронювання не знайдено"));

        cancelReservationAndClearBuffer(reservation);
    }

    private Boolean tryBook(MakeReservationRequest request) {
        if (request.date().getDayOfWeek() == DayOfWeek.SUNDAY) {
            throw new IllegalArgumentException("Клініка не працює в неділю");
        }
        if (request.date().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Не можна обрати минулу дату");
        }

        boolean booked = reservationBuffer.tryBook(
                request.doctorId(),
                request.date(),
                request.startTime(),
                request.serviceDurationMinutes()
        );

        if (!booked) {
            throw new RuntimeException("Цей час вже зайнятий, оберіть інший");
        }
        return true;
    }

    private void cancelReservationAndClearBuffer(Reservation reservation) {
        reservation.setCanceled(true);
        reservationRepository.save(reservation);

        int duration = (int) Duration.between(
                reservation.getStartTime(),
                reservation.getEndTime()
        ).toMinutes();

        // Звільняємо слот в буфері
        reservationBuffer.releaseSlot(
                reservation.getDoctor().getId(),
                reservation.getReservationDate(),
                reservation.getStartTime(),
                duration
        );
    }

}
