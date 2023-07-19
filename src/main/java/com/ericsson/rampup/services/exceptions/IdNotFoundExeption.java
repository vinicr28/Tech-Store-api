package com.ericsson.rampup.services.exceptions;

public class IdNotFoundExeption extends RuntimeException{

    public IdNotFoundExeption(Object id) {
        super("Entity id not found -> ID: " + id);
    }
}
