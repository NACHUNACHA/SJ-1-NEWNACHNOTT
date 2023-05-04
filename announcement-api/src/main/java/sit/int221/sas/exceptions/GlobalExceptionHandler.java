package sit.int221.sas.exceptions;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.sql.Timestamp;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(ItemNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> handleItemNotFoundException(ItemNotFoundException exception) {
        HttpHeaders respondHeaders = new HttpHeaders();
        respondHeaders.set("Description", exception.getMessage());
        return buildErrorResponse(HttpStatus.NOT_FOUND, respondHeaders, exception.getMessage());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleMethodArgumentTypeMismatch(
            MethodArgumentTypeMismatchException exception) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, new HttpHeaders(), "Invalid method argument type "  + exception.getCause().getMessage().toLowerCase());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Object> handleAllUncaughtException() {
        HttpHeaders respondHeaders = new HttpHeaders();
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, respondHeaders, "Unknown error occurred");
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleDataIntegrityViolationException() {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, new HttpHeaders(), "invalid input, object invalid");
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, new HttpHeaders(), "invalid input, object invalid");
    }

    private ResponseEntity<Object> buildErrorResponse(HttpStatus status, HttpHeaders headers, String message) {
        ErrorResponse errorResponse = new ErrorResponse(new Timestamp(System.currentTimeMillis()), status.value(), status.getReasonPhrase(), message);
        return ResponseEntity.status(status).headers(headers).body(errorResponse);
    }
}