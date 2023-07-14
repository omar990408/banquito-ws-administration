package ec.edu.espe.arquitectura.banquito.administration.handler;

import ec.edu.espe.arquitectura.banquito.administration.dto.res.BaseRes;
import ec.edu.espe.arquitectura.banquito.administration.exception.InsertException;
import ec.edu.espe.arquitectura.banquito.administration.exception.NotFoundException;
import ec.edu.espe.arquitectura.banquito.administration.exception.UpdateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
@Slf4j
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {
    /**
     * Handle Insert Exception
     *
     * @param exception InsertException
     * @param request   WebRequest
     * @return BaseRes
     */
    @ExceptionHandler(InsertException.class)
    public ResponseEntity<BaseRes> handleInsertException(InsertException exception, WebRequest request) {
        return new ResponseEntity<>(
                BaseRes.builder().message(exception.getMessage()).build(), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    /**
     * Handle Method Argument Not Valid Exception
     *
     * @param exception Exception
     * @param request   WebRequest
     * @return BaseRes
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseRes> handleMethodArgumentNotValidException(Exception exception, WebRequest request) {
        return new ResponseEntity<>(
                BaseRes.builder().message(exception.getMessage()).build(), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Handle Not Found
     *
     * @param exception NotFoundException
     * @param request   WebRequest
     * @return BaseRes
     */
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<BaseRes> handleNotFoundException(NotFoundException exception, WebRequest request) {
        return new ResponseEntity<>(
                BaseRes.builder().message(exception.getMessage()).build(), new HttpHeaders(), HttpStatus.NOT_FOUND);
    }

    /**
     * Handle Update Exception
     *
     * @param exception UpdateException
     * @param request   WebRequest
     * @return BaseRes
     */
    @ExceptionHandler(UpdateException.class)
    public ResponseEntity<BaseRes> handleUpdateException(UpdateException exception, WebRequest request) {
        return new ResponseEntity<>(
                BaseRes.builder().message(exception.getMessage()).build(), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }
}
