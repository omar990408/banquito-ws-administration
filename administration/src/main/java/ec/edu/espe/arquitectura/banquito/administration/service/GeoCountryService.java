package ec.edu.espe.arquitectura.banquito.administration.service;

import ec.edu.espe.arquitectura.banquito.administration.dto.req.GeoCountryReq;
import ec.edu.espe.arquitectura.banquito.administration.dto.res.BaseRes;
import ec.edu.espe.arquitectura.banquito.administration.exception.InsertException;
import ec.edu.espe.arquitectura.banquito.administration.model.GeoCountry;
import ec.edu.espe.arquitectura.banquito.administration.repository.GeoCountryRepository;
import ec.edu.espe.arquitectura.banquito.administration.service.mapper.GeoCountryMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static ec.edu.espe.arquitectura.banquito.administration.constant.MessageConstant.ERROR_REGISTRY;
import static ec.edu.espe.arquitectura.banquito.administration.constant.MessageConstant.SUCCESS_REGISTRY;

@Service
@RequiredArgsConstructor
@Slf4j
public class GeoCountryService {
    
    private final GeoCountryRepository geoCountryRepository;
    private final GeoCountryMapper geoCountryMapper;
    
    public BaseRes createCountry(GeoCountryReq geoCountryReq) throws InsertException {
        try{
            GeoCountry geoCountry = geoCountryMapper.toGeoCountry(geoCountryReq);
            geoCountryRepository.save(geoCountry);
            return BaseRes.builder().message(SUCCESS_REGISTRY).build();
        }catch (Exception e){
            throw new InsertException(ERROR_REGISTRY);
        }
    }


}
