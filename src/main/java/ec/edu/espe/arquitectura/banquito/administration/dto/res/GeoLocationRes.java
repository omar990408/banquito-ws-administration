package ec.edu.espe.arquitectura.banquito.administration.dto.res;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GeoLocationRes {
    private String uuid;
    private String countryCode;
    private String levelParentName;
    private String levelName;
    private String name;
    private String areaPhoneCode;
    private String zipCode;
}
