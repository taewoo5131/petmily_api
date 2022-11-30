package com.petmily.api.Exception;

import com.petmily.api.common.ResponseEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ExceptionController {
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity illegalArgumentExceptionHandler(IllegalArgumentException e) {
        log.info("[ExceptionController.illegalArgumentExceptionHandler] >> {} " , e.getMessage());
        return new ResponseEntity(ResponseEnum.ILLEGAL_ARGS_ERROR, HttpStatus.BAD_REQUEST);
    }
}
