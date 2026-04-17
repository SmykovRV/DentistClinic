package ffeks.smykov_rv.dentistclinic.reservation.buffer.impl;

import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// Буфер для одного лікаря на один день
@Getter
public class DoctorDayBufferImpl {
    private final Long doctorId;
    private final LocalDate date;
    private final List<TimeSlotImpl> slots;

    public DoctorDayBufferImpl(Long doctorId, LocalDate date, LocalTime workStart, LocalTime workEnd, int slotDurationMinutes) {
        this.doctorId = doctorId;
        this.date = date;
        this.slots = generateSlots(workStart, workEnd, slotDurationMinutes);
    }

    private List<TimeSlotImpl> generateSlots(LocalTime start, LocalTime end, int duration) {
        List<TimeSlotImpl> result = new ArrayList<>();
        LocalTime current = start;
        while (!current.plusMinutes(duration).isAfter(end)) {
            result.add(new TimeSlotImpl(current, current.plusMinutes(duration), true));
            current = current.plusMinutes(duration);
        }
        return result;
    }

    public List<TimeSlotImpl> getAvailableSlots() {
        return slots.stream()
                .filter(TimeSlotImpl::isAvailable)
                .toList();
    }

    public Optional<TimeSlotImpl> findSlot(LocalTime startTime) {
        return slots.stream()
                .filter(s -> s.getStartTime().equals(startTime))
                .findFirst();
    }
}