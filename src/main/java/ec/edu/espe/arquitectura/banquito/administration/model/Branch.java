package ec.edu.espe.arquitectura.banquito.administration.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@Document(collection = "branch")
public class Branch {
    @Id
    private String id;
    private String bankEntityId;
    private String locationId;
    @Indexed(unique = true)
    private String code;
    private String name;
    @Indexed(unique = true)
    private String uniqueKey;
    private String state;
    private LocalDate creationDate;
    private String emailAddress;
    private String phoneNumber;
    private String line1;
    private String line2;
    private BigDecimal latitude;
    private BigDecimal longitude;
    @Version
    private Long version;
}
