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

import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
        return this.holidayRepository.save(holiday);
    }
    public Holiday update (String uuid, HolidayReq holidayReq) {
        Optional<Holiday> holidayTmp = this.holidayRepository.findByUuid(uuid);
        if (holidayTmp.isEmpty()){
            throw new RuntimeException("No existe la locación");
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
                holiday.setCountryName(getCountryName(countryCode));
                holiday.setGeoLocationName(getLocationName(holiday.getGeoLocationId()));
            }
            return holidays;
        }
    }

    public List<HolidayRes> findByLocationId(String id){
        List<Holiday> holidayTmp = this.holidayRepository.findByGeoLocationId(id);
        if(holidayTmp.isEmpty()){
            throw new RuntimeException("No existe el código del pais");
        }else{
            List<HolidayRes> holidays  = this.holidayMapper.toHolidayResList(holidayTmp);
            for (HolidayRes holiday : holidays) {
                holiday.setCountryName(getCountryName(holiday.getCountryCode()));
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
            throw new RuntimeException("No existe la locación");
        }
        return countryTmp.get().getName();
    }
}
