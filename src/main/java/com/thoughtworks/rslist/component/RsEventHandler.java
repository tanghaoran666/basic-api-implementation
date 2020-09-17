package com.thoughtworks.rslist.component;

import com.thoughtworks.rslist.api.LoggingController;
import com.thoughtworks.rslist.exception.Error;
import com.thoughtworks.rslist.exception.RsEventNotValidException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class RsEventHandler {
    @ExceptionHandler({RsEventNotValidException.class})
    private ResponseEntity rsExceptionHandle(Exception e) {
        Logger logger = LogManager.getLogger(LoggingController.class);
        Error error = new Error();
        logger.catching(Level.ERROR,e);
        error.setError(e.getMessage());
        return ResponseEntity.badRequest().body(error);

    }
}
