package ec.edu.espe.arquitectura.banquito.administration.service.mapper;

import ec.edu.espe.arquitectura.banquito.administration.dto.req.GeoLocationReq;
import ec.edu.espe.arquitectura.banquito.administration.dto.res.GeoLocationRes;
import ec.edu.espe.arquitectura.banquito.administration.model.GeoLocation;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface GeoLocationMapper {
    GeoLocation toGeoLocation(GeoLocationReq geoLocationReq);

    GeoLocationRes toGeoLocationRes(GeoLocation geoLocation);
    List<GeoLocationRes> toGeoLocationResList(List<GeoLocation> geoLocations);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateGeoLocation(GeoLocationReq geoLocationReq, @MappingTarget GeoLocation geoLocation);
}
