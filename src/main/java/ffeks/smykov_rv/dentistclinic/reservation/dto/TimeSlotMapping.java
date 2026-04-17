package ffeks.smykov_rv.dentistclinic.reservation.dto;

import ffeks.smykov_rv.dentistclinic.reservation.buffer.impl.TimeSlotImpl;
import ffeks.smykov_rv.dentistclinic.reservation.dto.mapping.TimeSlotDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TimeSlotMapping {
    TimeSlotImpl toEntity(TimeSlotDto dto);
    TimeSlotDto toDto(TimeSlotImpl timeSlot);
}
