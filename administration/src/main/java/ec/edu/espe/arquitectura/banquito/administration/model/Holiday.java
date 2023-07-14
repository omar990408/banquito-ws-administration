package ec.edu.espe.arquitectura.banquito.administration.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Version;

import java.util.Date;

@Data
@Builder
public class Holiday {
    private Date holidayDate;
    private String name;
    private String type;
    @Version
    private Long version;
}
