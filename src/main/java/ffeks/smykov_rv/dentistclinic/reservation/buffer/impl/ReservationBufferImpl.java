package ffeks.smykov_rv.dentistclinic.reservation.buffer.impl;

import ffeks.smykov_rv.dentistclinic.reservation.model.Doctor;
import ffeks.smykov_rv.dentistclinic.reservation.model.Reservation;
import ffeks.smykov_rv.dentistclinic.reservation.repository.DoctorRepository;
import ffeks.smykov_rv.dentistclinic.reservation.repository.LocationsRepository;
import ffeks.smykov_rv.dentistclinic.reservation.repository.ReservationRepository;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

// Головний буфер — singleton
@Component
@Slf4j
public class ReservationBufferImpl {

    // doctorId -> date -> DoctorDayBuffer
    private final Map<Long, Map<LocalDate, DoctorDayBufferImpl>> buffer = new ConcurrentHashMap<>();

    private final ReservationRepository reservationRepository;
    private final DoctorRepository doctorRepository;
    private final LocationsRepository locationRepository;

    private static final LocalTime WORK_START = LocalTime.of(8, 0);
    private static final LocalTime WORK_END = LocalTime.of(18, 0);
    private static final int SLOT_DURATION = 30; // хвилин
    // Скільки днів наперед генеруємо слоти
    private static final int DAYS_AHEAD = 30;

    public ReservationBufferImpl(ReservationRepository reservationRepository,
                                 DoctorRepository doctorRepository,
                                 LocationsRepository locationRepository) {
        this.reservationRepository = reservationRepository;
        this.doctorRepository = doctorRepository;
        this.locationRepository = locationRepository;
    }

    // Викликається при старті сервера
    @PostConstruct
    public void initialize() {
        log.info("Initializing reservation buffer...");
        List<Doctor> doctors = doctorRepository.findAll();
        LocalDate today = LocalDate.now();

        for (Doctor doctor : doctors) {
            for (int i = 0; i < DAYS_AHEAD; i++) {
                LocalDate date = today.plusDays(i);
                // Пропускаємо неділю
                if (date.getDayOfWeek() == DayOfWeek.SUNDAY) continue;

                DoctorDayBufferImpl dayBuffer = new DoctorDayBufferImpl(
                        doctor.getId(), date, WORK_START, WORK_END, SLOT_DURATION
                );
                buffer
                        .computeIfAbsent(doctor.getId(), k -> new ConcurrentHashMap<>())
                        .put(date, dayBuffer);
            }
        }

        // Завантажуємо вже існуючі бронювання з БД і блокуємо слоти
        loadExistingReservations();
        log.info("Buffer initialized: {} doctors, {} days ahead", doctors.size(), DAYS_AHEAD);
    }

    private void loadExistingReservations() {
        LocalDate today = LocalDate.now();
        LocalDate until = today.plusDays(DAYS_AHEAD);

        List<Reservation> existing = reservationRepository
                .findByReservationDateBetweenAndIsCanceledFalse(today, until);

        for (Reservation r : existing) {
            int duration = (int) java.time.Duration.between(r.getStartTime(), r.getEndTime()).toMinutes();
            tryBook(r.getDoctor().getId(), r.getReservationDate(), r.getStartTime(), duration);
        }
    }

    // Отримати всі вільні слоти для лікаря на дату
    public List<TimeSlotImpl> getAvailableSlots(Long doctorId, LocalDate date) {
        DoctorDayBufferImpl dayBuffer = getDayBuffer(doctorId, date);
        if (dayBuffer == null) return List.of();
        return dayBuffer.getAvailableSlots();
    }

    // Спробувати забронювати слот (повертає true якщо успішно)
    public boolean tryBook(Long doctorId, LocalDate date, LocalTime startTime, int durationMinutes) {
        DoctorDayBufferImpl dayBuffer = getDayBuffer(doctorId, date);
        if (dayBuffer == null) return false;

        LocalTime endTime = startTime.plusMinutes(durationMinutes);

        // Знаходимо всі слоти які перекриваються з діапазоном startTime — endTime
        List<TimeSlotImpl> slotsToBook = dayBuffer.getSlots().stream()
                .filter(slot -> !slot.getStartTime().isBefore(startTime) && slot.getStartTime().isBefore(endTime))
                .toList();

        if (slotsToBook.isEmpty()) return false;

        // Перевіряємо що всі слоти вільні
        boolean allAvailable = slotsToBook.stream().allMatch(TimeSlotImpl::isAvailable);
        if (!allAvailable) return false;

        // Блокуємо всі слоти
        slotsToBook.forEach(TimeSlotImpl::tryBook);
        return true;
    }
    // Звільнити слот (при скасуванні бронювання)
    public void releaseSlot(Long doctorId, LocalDate date, LocalTime startTime, int durationMinutes) {
        DoctorDayBufferImpl dayBuffer = getDayBuffer(doctorId, date);
        if (dayBuffer == null) return;

        LocalTime endTime = startTime.plusMinutes(durationMinutes);

        dayBuffer.getSlots().stream()
                .filter(slot -> !slot.getStartTime().isBefore(startTime) && slot.getStartTime().isBefore(endTime))
                .forEach(TimeSlotImpl::release);
    }

    // Заблокувати слот без спроби бронювання (при завантаженні з БД)
    private void blockSlot(Long doctorId, LocalDate date, LocalTime startTime) {
        DoctorDayBufferImpl dayBuffer = getDayBuffer(doctorId, date);
        if (dayBuffer == null) return;

        dayBuffer.findSlot(startTime).ifPresent(slot -> slot.release()); // скидаємо і одразу блокуємо
        dayBuffer.findSlot(startTime).ifPresent(TimeSlotImpl::tryBook);
    }

    private DoctorDayBufferImpl getDayBuffer(Long doctorId, LocalDate date) {
        Map<LocalDate, DoctorDayBufferImpl> doctorBuffer = buffer.get(doctorId);
        if (doctorBuffer == null) return null;
        return doctorBuffer.get(date);
    }
}