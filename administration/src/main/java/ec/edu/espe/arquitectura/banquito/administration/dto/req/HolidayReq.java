package ec.edu.espe.arquitectura.banquito.administration.dto.req;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@Builder
public class HolidayReq implements Serializable {
    private static final long serialVersionUID = 1L;

    private Date holidayDate;
    private String countryCode;
    private String geoLocationId;
    private String name;
    private String type;
}
