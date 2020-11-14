package com.ediary.web.controllers;

import com.ediary.exceptions.NoAccessException;
import com.ediary.exceptions.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class ControllerExceptionHandler {

//    @ResponseStatus(HttpStatus.NOT_FOUND)
//    @ExceptionHandler(NotFoundException.class)
//    public ModelAndView handleNotFoundException(Exception ex) {
//        ModelAndView modelAndView = new ModelAndView();
//        modelAndView.setViewName("errors/notFoundError");
//        modelAndView.addObject("exception", ex);
//        return modelAndView;
//    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(NoAccessException.class)
    public ModelAndView handleAccessException(Exception ex) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("errors/noAccessError");
        modelAndView.addObject("exception", ex);
        return modelAndView;
    }

}
