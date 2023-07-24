package ec.edu.espe.arquitectura.banquito.administration.service.mapper;

import ec.edu.espe.arquitectura.banquito.administration.dto.req.GeoCountryReq;
import ec.edu.espe.arquitectura.banquito.administration.dto.res.GeoCountryRes;
import ec.edu.espe.arquitectura.banquito.administration.model.GeoCountry;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface GeoCountryMapper {

    GeoCountry toGeoCountry(GeoCountryReq geoCountryReq);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updatePais(GeoCountryReq geoCountryReq, @MappingTarget GeoCountry geoCountry);

    GeoCountryRes toGeoCountryRes(GeoCountry geoCountry);
    List<GeoCountryRes> toRes(List<GeoCountry> geoCountries);

}
