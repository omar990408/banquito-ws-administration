package ec.edu.espe.arquitectura.banquito.administration.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class HolidayDto {
    private Date holidayDate;
    private String name;
    private String type;
}
