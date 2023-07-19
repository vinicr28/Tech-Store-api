package com.ericsson.rampup.services.exceptions;

import com.ericsson.rampup.entities.ProductOffering;

public class NotForSaleExeption extends RuntimeException{

    public NotForSaleExeption(String msg) {
        super(msg);
    }
}
