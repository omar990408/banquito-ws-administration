package ec.edu.espe.arquitectura.banquito.administration.dto.req;

import ec.edu.espe.arquitectura.banquito.administration.model.GeoStructure;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class GeoLocationReq {
    private String countryCode;
    private List<GeoStructureReq> geoStructures;
}
