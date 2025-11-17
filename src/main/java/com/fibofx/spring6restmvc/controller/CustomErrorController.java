package com.fibofx.spring6restmvc.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class CustomErrorController {

@ExceptionHandler
    ResponseEntity handleJPAViolations(TransactionSystemException exception){

        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity handleBindError(MethodArgumentNotValidException e){
        List<Map<String, String>> errorList = e.getFieldErrors().stream()
                .map(fieldError -> {
                    Map<String,String> errorMap = new HashMap<>();
                    errorMap.put(fieldError.getField(),fieldError.getDefaultMessage());
                    return errorMap;
                }).toList();
        return ResponseEntity.badRequest().body(errorList);
    }

}
