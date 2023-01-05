package site.metacoding.junittest.web.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import site.metacoding.junittest.web.dto.response.CMRespDto;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> apiException(RuntimeException e){
        return new ResponseEntity<>(CMRespDto.builder().code(-1).msg(e.getMessage()).build(),
                HttpStatus.BAD_REQUEST);
    }
}
