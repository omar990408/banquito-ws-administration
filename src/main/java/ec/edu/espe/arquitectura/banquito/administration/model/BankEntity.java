package ec.edu.espe.arquitectura.banquito.administration.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document(collection = "bank_entity")
public class BankEntity {
    @Id
    private String id;
    private String name;
    @Indexed(unique = true)
    private String internationalCode;
}
