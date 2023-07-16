package ec.edu.espe.arquitectura.banquito.administration.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Version;

@Data
@Builder
public class GeoStructure {
    private Integer levelCode;
    private String name;
    @Version
    private long version;
}
