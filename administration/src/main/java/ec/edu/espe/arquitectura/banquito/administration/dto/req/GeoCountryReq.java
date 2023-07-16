package ec.edu.espe.arquitectura.banquito.administration.dto.req;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class GeoCountryReq {

    private String countryCode;
    private String name;
    private String phoneCode;
    private List<GeoStructureReq> geoStructures;
    
}
