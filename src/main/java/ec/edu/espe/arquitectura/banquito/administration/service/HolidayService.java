package ec.edu.espe.arquitectura.banquito.administration.service;

import ec.edu.espe.arquitectura.banquito.administration.dto.req.HolidayReq;
import ec.edu.espe.arquitectura.banquito.administration.dto.res.HolidayRes;
import ec.edu.espe.arquitectura.banquito.administration.model.GeoCountry;
import ec.edu.espe.arquitectura.banquito.administration.model.GeoLocation;
import ec.edu.espe.arquitectura.banquito.administration.model.Holiday;
import ec.edu.espe.arquitectura.banquito.administration.repository.GeoCountryRepository;
import ec.edu.espe.arquitectura.banquito.administration.repository.GeoLocationRepository;
import ec.edu.espe.arquitectura.banquito.administration.repository.HolidayRepository;
import ec.edu.espe.arquitectura.banquito.administration.service.mapper.HolidayMapper;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;

@Service
public class HolidayService {
    private final HolidayRepository holidayRepository;
    private final GeoLocationRepository geoLocationRepository;
    private final GeoCountryRepository geoCountryRepository;
    private final HolidayMapper holidayMapper;

    public HolidayService(HolidayRepository holidayRepository, GeoLocationRepository geoLocationRepository, GeoCountryRepository geoCountryRepository, HolidayMapper holidayMapper) {
        this.holidayRepository = holidayRepository;
        this.geoLocationRepository = geoLocationRepository;
        this.geoCountryRepository = geoCountryRepository;
        this.holidayMapper = holidayMapper;
    }

    public Holiday create(HolidayReq holidayReq){
        Holiday holiday = this.holidayMapper.toHoliday(holidayReq);
        holiday.setUuid(UUID.randomUUID().toString());
        holiday.setState("ACT");
        return this.holidayRepository.save(holiday);
    }
    public Holiday update (String uuid, HolidayReq holidayReq) {
        Optional<Holiday> holidayTmp = this.holidayRepository.findByUuid(uuid);
        if (holidayTmp.isEmpty()){
            throw new RuntimeException("No existe el feriado");
        }else {
            Holiday holiday = holidayTmp.get();
            holidayMapper.updateHoliday(holidayReq, holiday);
            return this.holidayRepository.save(holiday);
        }
    }

    public List<HolidayRes> findbyCountryCode(String countryCode){

        List<Holiday> holidayTmp = this.holidayRepository.findByCountryCode(countryCode);
        if(holidayTmp.isEmpty()){
            throw new RuntimeException("No existe el código del pais");
        }else{
            List<HolidayRes> holidays  = this.holidayMapper.toHolidayResList(holidayTmp);
            for (HolidayRes holiday : holidays) {
                if(holiday.getGeoLocationId() != null){
                    holiday.setGeoLocationName(getLocationName(holiday.getGeoLocationId()));
                }
                holiday.setCountryName(getCountryName(countryCode));
            }
            return holidays;
        }
    }

    public List<HolidayRes> findByLocationId(String id){
        List<Holiday> holidayTmp = this.holidayRepository.findByGeoLocationId(id);
        if(holidayTmp.isEmpty()){
            throw new RuntimeException("No existe la locación");
        }else{
            List<HolidayRes> holidays  = this.holidayMapper.toHolidayResList(holidayTmp);
            for (HolidayRes holiday : holidays) {
                if(holiday.getCountryCode() != null){
                    holiday.setCountryName(getCountryName(holiday.getCountryCode()));
                }
                holiday.setGeoLocationName(getLocationName(id));
            }
            return holidays;
        }
    }

    private String getLocationName(String geoLocationId) {
        Optional<GeoLocation> geoLocationTmp = this.geoLocationRepository.findByUuid(geoLocationId);
        if (geoLocationTmp.isEmpty()){
            throw new RuntimeException("No existe la locación");
        }
        return geoLocationTmp.get().getName();
    }

    private String getCountryName(String countryCode) {
        Optional<GeoCountry> countryTmp = this.geoCountryRepository.findByCountryCode(countryCode);
        if (countryTmp.isEmpty()){
            throw new RuntimeException("No existe el codigo del pais");
        }
        return countryTmp.get().getName();
    }

    public List<Holiday> generateHolidaysWeekendsCountries(
            int year,
            int month,
            boolean saturday,
            boolean sunday,
            String countryCode) {
        Calendar calendar = Calendar.getInstance();
        GeoCountry countryTmp = getCountryByCode(countryCode);
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
                        .uuid(UUID.randomUUID().toString())
                        .type("Nacional")
                        .state("ACT")
                        .name("Sabado Fin de Semana")
                        .countryCode(countryTmp.getCountryCode())
                        .build();
                if (this.findHolidayByHolidayUnique(holiday)) {
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
                        .type("Nacional")
                        .state("ACT")
                        .uuid(UUID.randomUUID().toString())
                        .name("Domingo Fin de Semana")
                        .countryCode(countryTmp.getCountryCode())
                        .build();
                if (this.findHolidayByHolidayUnique(holiday)) {
                    holidayWeekend.add(holiday);
                }
            }
            startDate = startDate.plusDays(1);
        }
        return this.holidayRepository.saveAll(holidayWeekend);
    }

    private GeoCountry getCountryByCode(String countryCode){
        Optional<GeoCountry> geoCountry = geoCountryRepository.findByCountryCode(countryCode);
        if (geoCountry.isPresent()) {
            return geoCountry.get();
        }else {
            throw new RuntimeException("El pais con código: "+countryCode+" no existe");
        }
    }

    private Boolean findHolidayByHolidayUnique(Holiday holiday) {

        Optional<Holiday> holidayTmp = this.holidayRepository.findByCountryCodeAndHolidayDate(holiday.getCountryCode(), holiday.getHolidayDate());
        return holidayTmp.isEmpty();

    }
    public Holiday deleteLogic(String uuid){
        Optional<Holiday> holidayTmp = this.holidayRepository.findByUuid(uuid);
        if (holidayTmp.isEmpty()){
            throw new RuntimeException("No existe el feriado");
        }
        Holiday holiday = holidayTmp.get();
        holiday.setState("INA");
        return this.holidayRepository.save(holiday);
    }
    public Holiday findByUuid(String uuid){
        Optional<Holiday> holidayTmp = this.holidayRepository.findByUuid(uuid);
        if (holidayTmp.isEmpty()){
            throw new RuntimeException("No existe el feriado");
        }
        return holidayTmp.get();
    }
}
