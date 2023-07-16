package ec.edu.espe.arquitectura.banquito.administration.service;

import ec.edu.espe.arquitectura.banquito.administration.dto.req.GeoLocationReq;
import ec.edu.espe.arquitectura.banquito.administration.dto.res.GeoLocationRes;
import ec.edu.espe.arquitectura.banquito.administration.model.GeoLocation;
import ec.edu.espe.arquitectura.banquito.administration.repository.GeoLocationRepository;
import ec.edu.espe.arquitectura.banquito.administration.service.mapper.GeoLocationMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class GeoLocationService {
    private final GeoLocationRepository geoLocationRepository;
    private final GeoLocationMapper geoLocationMapper;

    public GeoLocationService(GeoLocationRepository geoLocationRepository, GeoLocationMapper geoLocationMapper) {
        this.geoLocationRepository = geoLocationRepository;
        this.geoLocationMapper = geoLocationMapper;
    }

    @Transactional
    public GeoLocation create (GeoLocationReq geoLocationReq) {
        GeoLocation geoLocation = geoLocationMapper.toGeoLocation(geoLocationReq);
        return this.geoLocationRepository.save(geoLocation);
    }

    public List<GeoLocationRes> findByCountryCodeAndName(String name, String countryCode){
        List<GeoLocation> geoLocations = this.geoLocationRepository.findByGeoStructureCountryCode(name, countryCode);
        return this.geoLocationMapper.toGeoLocationResList(geoLocations);
    }







}
