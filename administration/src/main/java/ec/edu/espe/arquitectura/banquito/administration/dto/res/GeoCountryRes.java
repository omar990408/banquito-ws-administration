package ec.edu.espe.arquitectura.banquito.administration.dto.res;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class GeoCountryRes {
    private String countryCode;
    private String name;
    private String phoneCode;
    private List<GeoStructureRes> geoStructures;
}
