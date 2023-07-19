package com.ericsson.rampup.services.exceptions;

public class AlreadyHaveCustomerExeption extends RuntimeException{

    public AlreadyHaveCustomerExeption(Long id) {
        super("This user already has an active customer, with id: " + id);
    }
}
