package com.quantumsoft.hrms.Human_Resource_Website.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDate;
import java.time.LocalTime;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(AdminRecordNotFoundException.class)
    public ResponseEntity<CustomResponse> adminRecordNotFoundException(AdminRecordNotFoundException e, HttpServletRequest request){
        CustomResponse customResponse = new CustomResponse(LocalDate.now(), LocalTime.now(), HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND, e.getMessage(), request.getRequestURI());
        return new ResponseEntity<CustomResponse>(customResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(FirstTimeLoginException.class)
    public ResponseEntity<CustomResponse> firstTimeLoginException(FirstTimeLoginException e, HttpServletRequest request){
        CustomResponse customResponse = new CustomResponse(LocalDate.now(), LocalTime.now(), HttpStatus.NOT_ACCEPTABLE.value(), HttpStatus.NOT_ACCEPTABLE, e.getMessage(), request.getRequestURI());
        return new ResponseEntity<CustomResponse>(customResponse, HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<CustomResponse> resourceNotFoundException(ResourceNotFoundException e, HttpServletRequest request){
        CustomResponse customResponse = new CustomResponse(LocalDate.now(), LocalTime.now(), HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND, e.getMessage(), request.getRequestURI());
        return new ResponseEntity<CustomResponse>(customResponse, HttpStatus.NOT_FOUND);
    }
}
