package com.ericsson.rampup.services.exceptions;

public class EmailAlreadyRegisteredExeption extends RuntimeException{

    public EmailAlreadyRegisteredExeption(String email) {
        super("E-mail already registered: " + email);
    }
}
