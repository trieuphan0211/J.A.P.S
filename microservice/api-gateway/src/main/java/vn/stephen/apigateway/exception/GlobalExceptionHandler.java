package vn.stephen.apigateway.exception;

import lombok.extern.log4j.Log4j2;
import org.jose4j.lang.JoseException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import vn.stephen.apigateway.constants.ErrorCode;
import vn.stephen.apigateway.dto.ErrorResponse;

import java.util.Locale;

@Log4j2
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(vn.stephen.apigateway.exception.GlobalException.class)
    protected ResponseEntity<ErrorResponse> handleGlobalException(GlobalException globalException, Locale locale) {
        log.error("Global Exception: {}", globalException.getMessage());
        return ResponseEntity
                .badRequest()
                .body(ErrorResponse.builder()
                        .code(globalException.getCode())
                        .message(globalException.getMessage())
                        .build());
    }
    @ExceptionHandler(NullPointerException.class)
    protected ResponseEntity<ErrorResponse> handleNullPointerException(NullPointerException e, Locale locale) {
        return ResponseEntity
                .badRequest()
                .body(ErrorResponse.builder()
                        .code(ErrorCode.NULL_POINT_EXCEPTION)
                        .message(e.getLocalizedMessage())
                        .build());
    }
    @ExceptionHandler(JoseException.class)
    protected ResponseEntity<ErrorResponse> handleNullPointerException(JoseException e, Locale locale) {
        return ResponseEntity
                .badRequest()
                .body(ErrorResponse.builder()
                        .code(ErrorCode.JOSE_INVALID_CREDENTIAL)
                        .message(e.getLocalizedMessage())
                        .build());
    }
    @ExceptionHandler({Exception.class})
    protected ResponseEntity<ErrorResponse> handleException(Exception e, Locale locale) {
        log.error("Unhandled Exception: {}", e.getMessage(), e);
        return ResponseEntity
                .badRequest()
                .body(ErrorResponse.builder()
                        .code(ErrorCode.UNKNOWN)
                        .message(e.getLocalizedMessage())
                        .build());
    }
}
