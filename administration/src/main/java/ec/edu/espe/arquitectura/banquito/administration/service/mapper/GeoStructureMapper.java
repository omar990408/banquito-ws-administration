package ec.edu.espe.arquitectura.banquito.administration.service.mapper;

import ec.edu.espe.arquitectura.banquito.administration.dto.req.GeoStructureReq;
import ec.edu.espe.arquitectura.banquito.administration.model.GeoStructure;
import org.mapstruct.*;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface GeoStructureMapper {
    GeoStructure toGeoStructure(GeoStructureReq geoStructureReq);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updatePais(GeoStructureReq geoStructureReq, @MappingTarget GeoStructure geoStructure);

}
