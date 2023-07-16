package ec.edu.espe.arquitectura.banquito.administration.dto.res;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GeoStructureRes {
    private Integer levelCode;
    private String name;
}
