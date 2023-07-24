package ec.edu.espe.arquitectura.banquito.administration.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Builder
@Document(collection = "geo_country")
public class GeoCountry {
    @Id
    private String id;
    @Indexed(unique = true)
    private String countryCode;
    private String name;
    private String phoneCode;
    private String state;
    private List<GeoStructure> geoStructures;
}
