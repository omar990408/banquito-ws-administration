package ec.edu.espe.arquitectura.banquito.administration.service.mapper;

import ec.edu.espe.arquitectura.banquito.administration.dto.req.GeoStructureReq;
import ec.edu.espe.arquitectura.banquito.administration.model.GeoStructure;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface GeoStructureMapper {
    GeoStructure toGeoStructure(GeoStructureReq geoStructureReq);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updatePais(GeoStructureReq geoStructureReq, @MappingTarget GeoStructure geoStructure);

}
