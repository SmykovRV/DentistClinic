package ffeks.smykov_rv.dentistclinic.reservation.dto.mapping;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalTime;

@Getter
@AllArgsConstructor
public class TimeSlotDto{
    private LocalTime startTime;
    private LocalTime endTime;
}
