package ec.edu.espe.arquitectura.banquito.administration.dto.res;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class HolidayRes {
    private String uuid;
    private Date holidayDate;
    private String countryCode;
    private String countryName;
    private String geoLocationId;
    private String geoLocationName;
    private String name;
    private String type;
}
