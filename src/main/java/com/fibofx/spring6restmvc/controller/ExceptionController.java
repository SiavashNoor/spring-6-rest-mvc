package com.fibofx.spring6restmvc.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;



//whether to use this  class or use @ResponseStatus on the NotFoundException Class . im going to comment out this class


//@ControllerAdvice
public class ExceptionController {

 //   @ExceptionHandler(NotFoundException.class)
    public ResponseEntity handleNotFoundException(){
        System.out.println("in exception handler");
        return ResponseEntity.notFound().build();
    }
}
