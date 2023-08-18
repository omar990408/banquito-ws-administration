package ec.edu.espe.arquitectura.banquito.administration.dto.req;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GeoStructureReq {
    private Integer levelCode;
    private String name;
}
