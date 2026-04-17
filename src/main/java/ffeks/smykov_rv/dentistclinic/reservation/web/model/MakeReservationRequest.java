package ffeks.smykov_rv.dentistclinic.reservation.web.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import tools.jackson.databind.annotation.JsonDeserialize;
import tools.jackson.databind.ext.javatime.deser.LocalTimeDeserializer;

import java.time.LocalDate;
import java.time.LocalTime;

public record MakeReservationRequest(

        @NotNull
        Long doctorId,

        @NotNull
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate date,

        @NotNull
        @JsonDeserialize(using = LocalTimeDeserializer.class)
        @JsonFormat(pattern = "HH:mm[[:ss]]")
        LocalTime startTime,


        @NotNull
        Integer serviceDurationMinutes,// береться з обраної послуги

        @NotBlank
        String description
) {

}
