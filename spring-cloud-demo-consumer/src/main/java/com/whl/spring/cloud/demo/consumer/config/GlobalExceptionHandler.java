package com.whl.spring.cloud.demo.consumer.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Throwable.class)
    public String handleThrowable(Throwable t) {
        logger.error("handleThrowable: " + t.getMessage(), t);
        return HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase();
    }

}
