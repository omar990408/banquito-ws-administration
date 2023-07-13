package ec.edu.espe.arquitectura.banquito.administration.controller.dto.req;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.index.Indexed;

import java.math.BigDecimal;
import java.util.Date;
@Data
@Builder
public class BranchReq {
    private String code;
    private String name;
    private String state;
    private String emailAdress;
    private String phoneNumber;
    private String line1;
    private String line2;
    private BigDecimal latitude;
    private BigDecimal longitude;
}
