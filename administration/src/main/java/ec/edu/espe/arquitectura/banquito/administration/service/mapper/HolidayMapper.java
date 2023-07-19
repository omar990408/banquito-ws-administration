package ec.edu.espe.arquitectura.banquito.administration.service.mapper;

import ec.edu.espe.arquitectura.banquito.administration.dto.req.HolidayReq;
import ec.edu.espe.arquitectura.banquito.administration.dto.res.HolidayRes;
import ec.edu.espe.arquitectura.banquito.administration.model.Holiday;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface HolidayMapper {
    Holiday toHoliday(HolidayReq holidayReq);
    HolidayReq toHolidayDto(Holiday holiday);

    HolidayRes toHolidayRes(Holiday holiday);
    List<HolidayRes> toHolidayResList(List<Holiday> holidays);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateHoliday(HolidayReq holidayReq, @MappingTarget Holiday holiday);
}
