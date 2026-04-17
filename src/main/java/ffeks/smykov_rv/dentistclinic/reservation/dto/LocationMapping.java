package ffeks.smykov_rv.dentistclinic.reservation.dto;

import ffeks.smykov_rv.dentistclinic.reservation.dto.mapping.LocationDto;
import ffeks.smykov_rv.dentistclinic.reservation.model.Location;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {DoctorMapping.class, AdministratorMapping.class})
public interface LocationMapping {

//    @Mapping(target = "administrators", qualifiedByName = "toAdministratorDto")
//    @Mapping(target = "doctors", qualifiedByName = "toDoctorDto")
    LocationDto toLocationDto(Location location);
    Location toEntity(LocationDto locationDto);
}
