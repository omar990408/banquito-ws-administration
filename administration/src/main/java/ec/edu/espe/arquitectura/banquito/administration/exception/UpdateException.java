package ec.edu.espe.arquitectura.banquito.administration.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

@EqualsAndHashCode(callSuper = true)
@Data
public class UpdateException extends Exception{
    @Serial
    private static final long serialVersionUID = 1L;
    private final String tableName;


    public UpdateException(String message) {
        super(message);
        this.tableName = null;
    }

    public UpdateException(String tableName, String message) {
        super(message);
        this.tableName = tableName;
    }

    public UpdateException(String tableName, String message, Throwable cause) {
        super(message);
        this.tableName = tableName;
    }
}
