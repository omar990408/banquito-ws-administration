package ec.edu.espe.arquitectura.banquito.administration.controller.dto.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BaseRes implements Serializable {
    @Serial
    private final static long serialVersionUID = 1L;
    private String message;
}
