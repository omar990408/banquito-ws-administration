package ec.edu.espe.arquitectura.banquito.administration.exception;

import lombok.Getter;

import java.io.Serial;

@Getter
public class InsertException extends Exception {
    @Serial
    private static final long serialVersionUID = 1L;
    private final String tableName;

    public InsertException(String message) {
        super(message);
        this.tableName = null;
    }

    public InsertException(String tableName, String message) {
        super(message);
        this.tableName = tableName;
    }

    public InsertException(String tableName, String message, Throwable cause) {
        super(message);
        this.tableName = tableName;
    }
}
