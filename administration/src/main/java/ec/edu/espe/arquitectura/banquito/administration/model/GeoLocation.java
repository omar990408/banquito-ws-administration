package ec.edu.espe.arquitectura.banquito.administration.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document(collection = "geo_location")
public class GeoLocation {
    @Id
    private String id;
    private String uuid;
    private String countryCode;
    private String levelParentId;
    private String levelParentName;
    private String levelCode;
    private String levelName;
    private String name;
    private String areaPhoneCode;
    private String zipCode;
    @Version
    private Long version;
}
