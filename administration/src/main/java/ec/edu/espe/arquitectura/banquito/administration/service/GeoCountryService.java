package ec.edu.espe.arquitectura.banquito.administration.service;

import ec.edu.espe.arquitectura.banquito.administration.dto.req.GeoCountryReq;
import ec.edu.espe.arquitectura.banquito.administration.model.GeoCountry;
import ec.edu.espe.arquitectura.banquito.administration.repository.GeoCountryRepository;
import ec.edu.espe.arquitectura.banquito.administration.service.mapper.GeoCountryMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
public class GeoCountryService {
    
    private final GeoCountryRepository geoCountryRepository;
    private final GeoCountryMapper geoCountryMapper;

    public GeoCountryService(GeoCountryRepository geoCountryRepository, GeoCountryMapper geoCountryMapper) {
        this.geoCountryRepository = geoCountryRepository;
        this.geoCountryMapper = geoCountryMapper;
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
    public GeoCountry update (String id,GeoCountryReq geoCountryReq){
        GeoCountry geoCountry = getCountryByCode(id);
        this.geoCountryMapper.updatePais(geoCountryReq, geoCountry);
        return this.geoCountryRepository.save(geoCountry);
    }

    @Transactional
    public void delete(String id){
        GeoCountry geoCountry = getCountryByCode(id);
        this.geoCountryRepository.delete(geoCountry);
    }

    public GeoCountryReq findByCountryCode(String countryCode){
        GeoCountry geoCountry = getCountryByCode(countryCode);
        return this.geoCountryMapper.toGeoCountryReq(geoCountry);
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
