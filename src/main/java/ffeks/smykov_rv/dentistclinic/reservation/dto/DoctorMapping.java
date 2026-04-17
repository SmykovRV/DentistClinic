package ffeks.smykov_rv.dentistclinic.reservation.dto;

import ffeks.smykov_rv.dentistclinic.reservation.dto.mapping.DoctorDto;
import ffeks.smykov_rv.dentistclinic.reservation.model.Doctor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {UserAccountMapper.class})
public interface DoctorMapping {
    @Mapping(target = "userAccountDto", source = "userAccount") // Maps userAccount -> userAccountDto using the nested mapper
    @Mapping(target = "userAccount", ignore = true)
//    @Mapping(target = "location", ignore = true)
    DoctorDto toDoctorDto(Doctor doctor);
    Doctor toEntity(DoctorDto doctorDto);

}
