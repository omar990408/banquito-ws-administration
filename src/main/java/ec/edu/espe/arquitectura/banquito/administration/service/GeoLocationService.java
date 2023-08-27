package ec.edu.espe.arquitectura.banquito.administration.service;

import ec.edu.espe.arquitectura.banquito.administration.dto.req.GeoLocationReq;
import ec.edu.espe.arquitectura.banquito.administration.dto.res.GeoLocationRes;
import ec.edu.espe.arquitectura.banquito.administration.model.GeoLocation;
import ec.edu.espe.arquitectura.banquito.administration.repository.GeoLocationRepository;
import ec.edu.espe.arquitectura.banquito.administration.service.mapper.GeoLocationMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
public class GeoLocationService {
    private final GeoLocationRepository geoLocationRepository;
    private final GeoLocationMapper geoLocationMapper;

    public GeoLocationService(GeoLocationRepository geoLocationRepository, GeoLocationMapper geoLocationMapper) {
        this.geoLocationRepository = geoLocationRepository;
        this.geoLocationMapper = geoLocationMapper;
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

    public GeoLocation update (String uuid, GeoLocationReq geoLocationReq) {
        Optional<GeoLocation> geoLocationTmp = this.geoLocationRepository.findByUuid(uuid);
        if (geoLocationTmp.isEmpty()){
            throw new RuntimeException("No existe la locación");
        }else {
            GeoLocation geoLocation = geoLocationTmp.get();
            geoLocationMapper.updateGeoLocation(geoLocationReq, geoLocation);
            return this.geoLocationRepository.save(geoLocation);
        }
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

    public List<GeoLocationRes> findByCountryCodeAndLevelCode(String countryCode, String levelCode){
        List<GeoLocation> geoLocation = this.geoLocationRepository.findByCountryCodeAndLevelCode(countryCode, levelCode);
        if (geoLocation.isEmpty()){
            throw new RuntimeException("No se encontro resultados");
        }
        return this.geoLocationMapper.toGeoLocationResList(geoLocation);
    }

    public void delete(String uuid){
        Optional<GeoLocation> geoLocation = this.geoLocationRepository.findByUuid(uuid);
        if (geoLocation.isEmpty()){
            throw new RuntimeException("No se encontro resultados");
        }
        this.geoLocationRepository.delete(geoLocation.get());
    }
}
