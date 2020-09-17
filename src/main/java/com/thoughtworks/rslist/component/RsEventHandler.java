package com.thoughtworks.rslist.component;

import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.exception.Error;
import com.thoughtworks.rslist.exception.RsEventNotValidException;
import com.thoughtworks.rslist.po.UserPo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class RsEventHandler {
    @ExceptionHandler({RsEventNotValidException.class, MethodArgumentNotValidException.class})
    private ResponseEntity rsExceptionHandle(Exception e) {
        Error error = new Error();
        if(e instanceof RsEventNotValidException){
            error.setError(e.getMessage());
        }
        else if (e instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException methodArgumentNotValidException = (MethodArgumentNotValidException) e;

            if(methodArgumentNotValidException.getBindingResult().getTarget() instanceof UserPo) {
                error.setError("invalid user");
            }else if (methodArgumentNotValidException.getBindingResult().getTarget() instanceof RsEvent){
                error.setError("invalid param");
            }

         }
        Logger logger = LogManager.getLogger(RsEventHandler.class);
//        logger.catching(Level.ERROR,e);
        logger.error("here is a error!"+e.getMessage());
        return ResponseEntity.badRequest().body(error);

    }
}
