package ec.edu.espe.arquitectura.banquito.administration.dto.req;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Builder
public class BranchReq implements Serializable {
    private static final long serialVersionUID = 1L;

    private String code;
    private String bankEntityId;
    private String locationId;
    private String name;
    private String state;
    private String emailAddress;
    private String phoneNumber;
    private String line1;
    private String line2;
    private BigDecimal latitude;
    private BigDecimal longitude;
}
