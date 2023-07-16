package ec.edu.espe.arquitectura.banquito.administration.service.mapper;

import ec.edu.espe.arquitectura.banquito.administration.dto.HolidayDto;
import ec.edu.espe.arquitectura.banquito.administration.model.Holiday;
import org.mapstruct.*;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface HolidayMapper {
    Holiday toHoliday(HolidayDto holidayDto);
    HolidayDto toHolidayDto(Holiday holiday);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateHoliday(HolidayDto holidayReq, @MappingTarget Holiday holiday);
}
