package com.siemash.cvrpvisualizer.config;

import com.siemash.cvrpvisualizer.controller.dto.ErrorMessageDto;
import com.siemash.cvrpvisualizer.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class CommonControllerAdvice {

    @ExceptionHandler({NotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    ErrorMessageDto handleException(Exception ex) {
        return new ErrorMessageDto(ex.getMessage());
    }
}
