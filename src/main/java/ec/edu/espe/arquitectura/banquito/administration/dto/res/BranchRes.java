package ec.edu.espe.arquitectura.banquito.administration.dto.res;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class BranchRes {
    private String locationId;
    private String locationName;
    private String code;
    private String name;
    private String emailAddress;
    private String phoneNumber;
    private String line1;
    private String line2;
    private BigDecimal latitude;
    private BigDecimal longitude;
}
