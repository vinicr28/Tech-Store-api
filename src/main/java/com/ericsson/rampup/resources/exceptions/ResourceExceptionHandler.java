package com.ericsson.rampup.resources.exceptions;


import com.ericsson.rampup.services.exceptions.*;

import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;
import java.util.NoSuchElementException;

@ControllerAdvice
public class ResourceExceptionHandler {

    private static final String ENTITY_NOT_FOUND = "Entity not found.";
    private static final String OUT_OF_STOCK = "Product out of stock!";
    private static final String REGISTERED = "Already registered!";
    private static final String ILLEGAL_ARGUMENT = "Illegal argument!";
    private static final String VALUE_IS_NULL = "Value is null!";

    @ExceptionHandler(IdNotFoundExeption.class)
    public ResponseEntity<StandardError> ResourceNotFound(IdNotFoundExeption e, HttpServletRequest request){

        StandardError se = new StandardError(Instant.now(), HttpStatus.NOT_FOUND.value() , ENTITY_NOT_FOUND, e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(se);

    }

    @ExceptionHandler(NotForSaleExeption.class)
    public ResponseEntity<StandardError> notForSale(NotForSaleExeption e, HttpServletRequest request){

        StandardError se = new StandardError(Instant.now(), HttpStatus.OK.value(), OUT_OF_STOCK, e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(se);

    }

    @ExceptionHandler(EmailAlreadyRegisteredExeption.class)
    public ResponseEntity<StandardError> emailAlreadyRegistered(EmailAlreadyRegisteredExeption e, HttpServletRequest request){

        StandardError se = new StandardError(Instant.now(), HttpStatus.OK.value(), REGISTERED, e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(se);

    }

    @ExceptionHandler(NotFoundExeption.class)
    public ResponseEntity<StandardError> notFound(NotFoundExeption e, HttpServletRequest request){

        StandardError se = new StandardError(Instant.now(), HttpStatus.NOT_FOUND.value(), ENTITY_NOT_FOUND, e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(se);

    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<StandardError> illegalArgument(IllegalArgumentException e, HttpServletRequest request){

        StandardError se = new StandardError(Instant.now(), HttpStatus.BAD_REQUEST.value(), ILLEGAL_ARGUMENT, e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(se);

    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<StandardError> nullPointerException(NullPointerException e, HttpServletRequest request){

        StandardError se = new StandardError(Instant.now(), HttpStatus.BAD_REQUEST.value(), e.getMessage(), "value must not be null!", request.getRequestURI());
        return ResponseEntity.status(500).body(se);

    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<StandardError> noSuchElementException(NoSuchElementException e, HttpServletRequest request){

        StandardError se = new StandardError(Instant.now(), HttpStatus.BAD_REQUEST.value(), e.getMessage(), "No such element!", request.getRequestURI());
        return ResponseEntity.status(500).body(se);

    }

    @ExceptionHandler(AlreadyHaveCustomerExeption.class)
    public ResponseEntity<StandardError> alreadyHaveCustomerExeption(AlreadyHaveCustomerExeption e, HttpServletRequest request){

        StandardError se = new StandardError(Instant.now(), HttpStatus.BAD_REQUEST.value(), e.getMessage(), "First delete the current customer to create a new one!", request.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(se);
    }



}
