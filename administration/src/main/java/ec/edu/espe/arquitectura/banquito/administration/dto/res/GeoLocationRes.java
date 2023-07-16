package ec.edu.espe.arquitectura.banquito.administration.dto.res;

import ec.edu.espe.arquitectura.banquito.administration.model.GeoStructure;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class GeoLocationRes {
    private String countryCode;
    private String levelName;
    private String levelParentName;
    private String name;
    private String areaPhoneCode;
    private String zipCode;
}
