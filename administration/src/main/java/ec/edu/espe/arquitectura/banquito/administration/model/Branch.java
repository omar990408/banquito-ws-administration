package ec.edu.espe.arquitectura.banquito.administration.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.index.Indexed;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

@Data
@Builder
public class Branch {

    @Indexed(unique = true)
    private String code;
    private String name;
    @Indexed(unique = true)
    private String uniqueKey;
    private String state;
    private LocalDate creationDate;
    private String emailAdress;
    private String phoneNumber;
    private String line1;
    private String line2;
    private BigDecimal latitude;
    private BigDecimal longitude;
}
