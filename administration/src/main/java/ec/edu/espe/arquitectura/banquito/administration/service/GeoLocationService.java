package ec.edu.espe.arquitectura.banquito.administration.service;

import ec.edu.espe.arquitectura.banquito.administration.dto.req.HolidayReq;
import ec.edu.espe.arquitectura.banquito.administration.dto.req.GeoLocationReq;
import ec.edu.espe.arquitectura.banquito.administration.dto.res.GeoLocationRes;
import ec.edu.espe.arquitectura.banquito.administration.model.GeoLocation;
import ec.edu.espe.arquitectura.banquito.administration.model.Holiday;
import ec.edu.espe.arquitectura.banquito.administration.repository.GeoLocationRepository;
import ec.edu.espe.arquitectura.banquito.administration.service.mapper.GeoLocationMapper;
import ec.edu.espe.arquitectura.banquito.administration.service.mapper.HolidayMapper;
import org.springframework.stereotype.Service;


import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;


@Service
public class GeoLocationService {
    private final GeoLocationRepository geoLocationRepository;
    private final GeoLocationMapper geoLocationMapper;
    private final HolidayMapper holidayMapper;

    public GeoLocationService(GeoLocationRepository geoLocationRepository, GeoLocationMapper geoLocationMapper, HolidayMapper holidayMapper) {
        this.geoLocationRepository = geoLocationRepository;
        this.geoLocationMapper = geoLocationMapper;
        this.holidayMapper = holidayMapper;
    }


    public GeoLocation create (GeoLocationReq geoLocationReq) {
        Optional<GeoLocation> geoLocationTmp = this.geoLocationRepository.
                findByCountryCodeAndName(geoLocationReq.getCountryCode(), geoLocationReq.getName());
        if (geoLocationTmp.isPresent()){
            throw new RuntimeException("Ya existe un registro con el mismo nombre");
        }else {
            GeoLocation geoLocation = geoLocationMapper.toGeoLocation(geoLocationReq);
            geoLocation.setUuid(UUID.randomUUID().toString());
            return this.geoLocationRepository.save(geoLocation);
        }
    }

    public GeoLocation update (String id, GeoLocationReq geoLocationReq) {
        Optional<GeoLocation> geoLocationTmp = this.geoLocationRepository.findById(id);
        if (geoLocationTmp.isEmpty()){
            throw new RuntimeException("No existe la locación");
        }else {
            GeoLocation geoLocation = geoLocationTmp.get();
            geoLocationMapper.updateGeoLocation(geoLocationReq, geoLocation);
            return this.geoLocationRepository.save(geoLocation);
        }
    }


    public GeoLocation addHoliday(String id, HolidayReq holidayReq){
        GeoLocation geoLocation = this.geoLocationRepository.findById(id).orElseThrow(() -> new RuntimeException("No existe la locación"));
        Holiday holiday = this.holidayMapper.toHoliday(holidayReq);
        if(geoLocation.getHolidays() == null){
            geoLocation.setHolidays(new ArrayList<>());
        }
        List<Holiday> holidaysList= geoLocation.getHolidays();
        if (!holidaysList.isEmpty()) {
            for (Holiday holidayTmp : holidaysList) {
                if (holidayTmp.getHolidayDate().equals(holiday.getHolidayDate())) {
                    throw new RuntimeException("Ya existe un feriado con la fecha: " + holiday.getHolidayDate() + " para la locación: " + geoLocation.getName());
                }
            }
        }
        geoLocation.getHolidays().add(holiday);
        return this.geoLocationRepository.save(geoLocation);
    }

    public GeoLocation updateHoliday(String id, Date date, HolidayReq holidayReq){
        GeoLocation geoLocation = this.geoLocationRepository.findById(id).orElseThrow(() -> new RuntimeException("No existe la locación"));
        if(geoLocation.getHolidays() == null){
            geoLocation.setHolidays(new ArrayList<>());
        }
        List<Holiday> holidaysList= geoLocation.getHolidays();
        boolean existHoliday = false;
        if (!holidaysList.isEmpty()) {
            for (Holiday holidayTmp : holidaysList) {
                if (holidayTmp.getHolidayDate().equals(date)) {
                    this.holidayMapper.updateHoliday(holidayReq, holidayTmp);
                    existHoliday = true;
                    break;
                }
            }
            if (!existHoliday) {
                throw new RuntimeException("No existe un feriado con la fecha: " + date + " para la locación: " + geoLocation.getName());
            }else {
                geoLocation.setHolidays(holidaysList);
                return this.geoLocationRepository.save(geoLocation);
            }
        }else{
            throw new RuntimeException("No existe un feriado con la fecha: " + date + " para la locación " + geoLocation.getName());
        }
    }

    public GeoLocation generateHolidaysWeekends(
            String id,
            int year,
            int month,
            boolean saturday,
            boolean sunday){
        Calendar calendar = Calendar.getInstance();
        GeoLocation geoLocation = this.geoLocationRepository.findById(id).orElseThrow(() -> new RuntimeException("No existe la locación"));
        if(geoLocation.getHolidays() == null){
            geoLocation.setHolidays(new ArrayList<>());
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
                if (this.compareHolidays(holiday,geoLocation.getHolidays())) {
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
                if (this.compareHolidays(holiday,geoLocation.getHolidays())) {
                    holidayWeekend.add(holiday);
                }
            }
            startDate = startDate.plusDays(1);
        }
        geoLocation.getHolidays().addAll(holidayWeekend);
        return this.geoLocationRepository.save(geoLocation);
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
    public GeoLocationRes findByCountryCodeAndName(String countryCode,String name){
        Optional<GeoLocation> geoLocation = this.geoLocationRepository.findByCountryCodeAndName(countryCode, name);
        if (geoLocation.isEmpty()){
            throw new RuntimeException("No se encontro resultados");
        }
        return this.geoLocationMapper.toGeoLocationRes(geoLocation.get());
    }

    public List<GeoLocationRes> findByCountryCodeAndLevelParentName(String countryCode, String levelParentName){
        List<GeoLocation> geoLocation = this.geoLocationRepository.findByCountryCodeAndLevelParentName(countryCode, levelParentName);
        return this.geoLocationMapper.toGeoLocationResList(geoLocation);
    }

    public GeoLocationRes findByUuid(String uuid){
        Optional<GeoLocation> geoLocation = this.geoLocationRepository.findByUuid(uuid);
        if (geoLocation.isEmpty()){
            throw new RuntimeException("No se encontro resultados");
        }
        return this.geoLocationMapper.toGeoLocationRes(geoLocation.get());
    }

}
