package ec.edu.espe.arquitectura.banquito.administration.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Builder
@Document(collection = "holiday")
public class Holiday {
    @Id
    private String id;
    private String uuid;
    private Date holidayDate;
    private String countryCode;
    private String geoLocationId;
    private String name;
    private String type;
    private String state;
    @Version
    private Long version;
}
