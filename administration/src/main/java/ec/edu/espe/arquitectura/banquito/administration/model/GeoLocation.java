package ec.edu.espe.arquitectura.banquito.administration.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Builder
@Document(collection = "geo_location")
public class GeoLocation {
    @Id
    private String id;
    private String countryCode;
    private List<GeoStructure> geoStructures;
    @Version
    private Long version;
}
