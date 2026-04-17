package ffeks.smykov_rv.dentistclinic.reservation.buffer.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalTime;

// Один часовий слот
@Getter
@AllArgsConstructor
public class TimeSlotImpl {
    private final LocalTime startTime;
    private final LocalTime endTime;
    private volatile boolean available;

    public synchronized boolean tryBook() {
        if (!available) return false;
        available = false;
        return true;
    }

    public synchronized void release() {
        available = true;
    }
}