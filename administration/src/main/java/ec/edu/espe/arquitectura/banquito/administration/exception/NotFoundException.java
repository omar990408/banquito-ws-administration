package ec.edu.espe.arquitectura.banquito.administration.exception;

import java.io.Serial;

public class NotFoundException extends Exception {

    @Serial
    private static final long serialVersionUID = 1L;

    public NotFoundException(String message) {
        super(message);
    }
}

