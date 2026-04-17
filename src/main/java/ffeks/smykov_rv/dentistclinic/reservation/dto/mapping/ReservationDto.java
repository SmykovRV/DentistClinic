package ffeks.smykov_rv.dentistclinic.reservation.dto.mapping;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Getter
@Setter
public class ReservationDto {

    long id;
    String reservationDescription;
    LocalDate reservationDate;
    LocalTime startTime;
    LocalTime endTime;
    LocationDto location;
    DoctorDto doctor;
    UserAccountDto userAccountDto;

    @JsonProperty("isAccepted")
    boolean isAccepted;
    @JsonProperty("isCancelled")
    boolean isCancelled;
}
