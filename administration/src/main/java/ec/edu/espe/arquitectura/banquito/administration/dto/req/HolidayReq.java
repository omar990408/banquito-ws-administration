package ec.edu.espe.arquitectura.banquito.administration.dto.req;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class HolidayReq {
    private Date holidayDate;
    private String countryCode;
    private String geoLocationId;
    private String name;
    private String type;
}
