package ec.edu.espe.arquitectura.banquito.administration.service;

import ec.edu.espe.arquitectura.banquito.administration.dto.req.GeoCountryReq;
import ec.edu.espe.arquitectura.banquito.administration.dto.res.GeoCountryRes;
import ec.edu.espe.arquitectura.banquito.administration.model.GeoCountry;
import ec.edu.espe.arquitectura.banquito.administration.repository.GeoCountryRepository;
import ec.edu.espe.arquitectura.banquito.administration.service.mapper.GeoCountryMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class GeoCountryService {
    
    private final GeoCountryRepository geoCountryRepository;
    private final GeoCountryMapper geoCountryMapper;

    public GeoCountryService(GeoCountryRepository geoCountryRepository, GeoCountryMapper geoCountryMapper) {
        this.geoCountryRepository = geoCountryRepository;
        this.geoCountryMapper = geoCountryMapper;
    }


    public GeoCountry create(GeoCountryReq geoCountryReq){
        Optional<GeoCountry> geoCountryTmp = this.geoCountryRepository.findByCountryCode(geoCountryReq.getCountryCode());
        if(geoCountryTmp.isPresent()){
            throw new RuntimeException("Ya existe un pais con el codigo: "+geoCountryReq.getCountryCode());
        }else{
            GeoCountry geoCountry = geoCountryMapper.toGeoCountry(geoCountryReq);
            geoCountry.setState("ACT");
            return this.geoCountryRepository.save(geoCountry);
        }
    }

    public GeoCountry update (String code,GeoCountryReq geoCountryReq){
        GeoCountry geoCountry = getCountryByCode(code);
        this.geoCountryMapper.updatePais(geoCountryReq, geoCountry);
        return this.geoCountryRepository.save(geoCountry);
    }

    public GeoCountry deleteLogic(String code){
        GeoCountry geoCountry = getCountryByCode(code);
        geoCountry.setState("INA");
        return this.geoCountryRepository.save(geoCountry);
    }

    public void delete(String code){
        GeoCountry geoCountry = getCountryByCode(code);
        this.geoCountryRepository.delete(geoCountry);
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

    public List<GeoCountryRes> getCountries(){
        return this.geoCountryMapper.toRes(this.geoCountryRepository.findAllByStateContaining("ACT"));
    }

}
