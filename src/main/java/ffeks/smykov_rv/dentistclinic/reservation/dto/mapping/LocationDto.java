package ffeks.smykov_rv.dentistclinic.reservation.dto.mapping;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class LocationDto {
    Long id;
    String locationAddress;
    LocalTime workTimeStart;
    LocalTime workTimeEnd;
    String city;
    String district;
    String phoneNumber;
    Set<DoctorDto> doctors = new HashSet<>();
    Set<AdministratorDto> administrators = new HashSet<>();
}
