package ksh.deliveryhub.common.exception;

import ksh.deliveryhub.common.dto.response.ErrorResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;



@RestControllerAdvice
@Slf4j
public class GlobalControllerAdvice {

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorResponseDto> handleBindException(BindException ex) {
        String errorMessage = ex.getBindingResult()
            .getAllErrors()
            .get(0)
            .getDefaultMessage();

        log.info("예외 정보: {}", errorMessage);

        ErrorResponseDto response = ErrorResponseDto.of(
            HttpStatus.BAD_REQUEST.value(),
            HttpStatus.BAD_REQUEST.name(),
            errorMessage
        );

        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(response);
    }
}
