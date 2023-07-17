package ec.edu.espe.arquitectura.banquito.administration.service;

import ec.edu.espe.arquitectura.banquito.administration.dto.HolidayDto;
import ec.edu.espe.arquitectura.banquito.administration.dto.req.GeoCountryReq;
import ec.edu.espe.arquitectura.banquito.administration.dto.res.GeoCountryRes;
import ec.edu.espe.arquitectura.banquito.administration.model.GeoCountry;
import ec.edu.espe.arquitectura.banquito.administration.model.Holiday;
import ec.edu.espe.arquitectura.banquito.administration.repository.GeoCountryRepository;
import ec.edu.espe.arquitectura.banquito.administration.service.mapper.GeoCountryMapper;
import ec.edu.espe.arquitectura.banquito.administration.service.mapper.HolidayMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;


@Service
public class GeoCountryService {
    
    private final GeoCountryRepository geoCountryRepository;
    private final GeoCountryMapper geoCountryMapper;
    private final HolidayMapper holidayMapper;

    public GeoCountryService(GeoCountryRepository geoCountryRepository, GeoCountryMapper geoCountryMapper, HolidayMapper holidayMapper) {
        this.geoCountryRepository = geoCountryRepository;
        this.geoCountryMapper = geoCountryMapper;
        this.holidayMapper = holidayMapper;
    }

    @Transactional
    public GeoCountry create(GeoCountryReq geoCountryReq){
        Optional<GeoCountry> geoCountryTmp = this.geoCountryRepository.findByCountryCode(geoCountryReq.getCountryCode());
        if(geoCountryTmp.isPresent()){
            throw new RuntimeException("Ya existe un pais con el codigo: "+geoCountryReq.getCountryCode());
        }else{
            GeoCountry geoCountry = geoCountryMapper.toGeoCountry(geoCountryReq);
            return this.geoCountryRepository.save(geoCountry);
        }
    }

    @Transactional
    public GeoCountry update (String code,GeoCountryReq geoCountryReq){
        GeoCountry geoCountry = getCountryByCode(code);
        this.geoCountryMapper.updatePais(geoCountryReq, geoCountry);
        return this.geoCountryRepository.save(geoCountry);
    }

    @Transactional
    public void delete(String code){
        GeoCountry geoCountry = getCountryByCode(code);
        this.geoCountryRepository.delete(geoCountry);
    }

    @Transactional
    public GeoCountry addHoliday(String code, HolidayDto holidayDto){
        GeoCountry geoCountry = getCountryByCode(code);
        Holiday holiday = this.holidayMapper.toHoliday(holidayDto);
        if(geoCountry.getHolidays() == null){
            geoCountry.setHolidays(new ArrayList<>());
        }
        List<Holiday> holidaysList= geoCountry.getHolidays();
        if (!holidaysList.isEmpty()) {
            for (Holiday holidayTmp : holidaysList) {
                if (holidayTmp.getHolidayDate().equals(holiday.getHolidayDate())) {
                    throw new RuntimeException("Ya existe un feriado con la fecha: " + holiday.getHolidayDate() + " para el pais: " + geoCountry.getName());
                }
            }
        }
        geoCountry.getHolidays().add(holiday);
        return this.geoCountryRepository.save(geoCountry);
    }

    @Transactional
    public GeoCountry updateHoliday(String code, Date date,HolidayDto holidayDto){
        GeoCountry geoCountry = getCountryByCode(code);
        if(geoCountry.getHolidays() == null){
            geoCountry.setHolidays(new ArrayList<>());
        }
        List<Holiday> holidaysList= geoCountry.getHolidays();
        boolean existHoliday = false;
        if (!holidaysList.isEmpty()) {
            for (Holiday holidayTmp : holidaysList) {
                if (holidayTmp.getHolidayDate().equals(date)) {
                    this.holidayMapper.updateHoliday(holidayDto, holidayTmp);
                    existHoliday = true;
                    break;
                }
            }
            if (!existHoliday) {
                throw new RuntimeException("No existe un feriado con la fecha: " + date + " para el pais: " + geoCountry.getName());
            }else {
                geoCountry.setHolidays(holidaysList);
                return this.geoCountryRepository.save(geoCountry);
            }
        }else{
            throw new RuntimeException("No existe un feriado con la fecha: " + date + " para el pais: " + geoCountry.getName());
        }
    }

    public GeoCountry generateHolidaysWeekends(
            String code,
            int year,
            int month,
            boolean saturday,
            boolean sunday){
        Calendar calendar = Calendar.getInstance();
        GeoCountry geoCountry = getCountryByCode(code);
        if(geoCountry.getHolidays() == null){
            geoCountry.setHolidays(new ArrayList<>());
        }
        List<Holiday> holidayWeekend = new ArrayList<>();
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
        while (startDate.isBefore(endDate) || startDate.isEqual(endDate)) {
            if (saturday && startDate.getDayOfWeek() == DayOfWeek.SATURDAY) {
                calendar.set(startDate.getYear(), startDate.getMonthValue() - 1, startDate.getDayOfMonth());
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                Holiday holiday = Holiday.builder()
                        .holidayDate(calendar.getTime())
                        .type("NAT")
                        .name("SATURDAY WEEKEND")
                        .build();
                if (this.compareHolidays(holiday,geoCountry.getHolidays())) {
                    holidayWeekend.add(holiday);
                }
            }
            if (sunday && startDate.getDayOfWeek() == DayOfWeek.SUNDAY) {
                calendar.set(startDate.getYear(), startDate.getMonthValue() - 1, startDate.getDayOfMonth());
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                Holiday holiday = Holiday.builder().
                        holidayDate(calendar.getTime())
                        .type("NAT")
                        .name("SUNDAY WEEKEND")
                        .build();
                if (this.compareHolidays(holiday,geoCountry.getHolidays())) {
                    holidayWeekend.add(holiday);
                }
            }
            startDate = startDate.plusDays(1);
        }
        geoCountry.getHolidays().addAll(holidayWeekend);
        return this.geoCountryRepository.save(geoCountry);
    }

    public Boolean compareHolidays(Holiday holiday, List<Holiday> holidaysList) {
        boolean flag = true;
        for (Holiday holidayTmp: holidaysList) {
            if (holidayTmp.getHolidayDate().equals(holiday.getHolidayDate())) {
                flag = false;
                break;
            }
        }
        return flag;
    }
    public GeoCountryRes findByCountryCode(String countryCode){
        GeoCountry geoCountry = getCountryByCode(countryCode);
        return this.geoCountryMapper.toGeoCountryRes(geoCountry);
    }

    private GeoCountry getCountryByCode(String countryCode){
        Optional<GeoCountry> geoCountry = geoCountryRepository.findByCountryCode(countryCode);
        if (geoCountry.isPresent()) {
            return geoCountry.get();
        }else {
            throw new RuntimeException("El pais con código: "+countryCode+" no existe");
        }
    }

}
