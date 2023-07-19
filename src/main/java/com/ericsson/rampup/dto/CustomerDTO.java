package com.ericsson.rampup.dto;

import com.ericsson.rampup.entities.Customer;
import com.ericsson.rampup.entities.enums.CustomerType;
import com.ericsson.rampup.resources.view.View;
import com.fasterxml.jackson.annotation.JsonView;

import java.io.Serial;
import java.io.Serializable;

public class CustomerDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @JsonView(View.Base.class)
    private Long id;

    @JsonView(View.Base.class)
    private String customerName;

    @JsonView(View.Base.class)
    private Long documentNumber;

    @JsonView(View.Base.class)
    private String customerStatus;

    @JsonView(View.Base.class)
    private CustomerType customerType;

    @JsonView(View.Base.class)
    private String creditScore;

    private Long userId;


    public CustomerDTO() {
    }

    public CustomerDTO(Customer obj) {
        this.id = obj.getId();
        this.customerName = obj.getCustomerName();
        this.documentNumber = obj.getDocumentNumber();
        this.customerStatus = obj.getCustomerStatus();
        this.customerType = obj.getCustomerType();
        this.creditScore = obj.getCreditScore();
    }

    public Long getId() {
        return id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Long getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(Long documentNumber) {
        this.documentNumber = documentNumber;
    }

    public String getCustomerStatus() {
        return customerStatus;
    }

    public void setCustomerStatus(String customerStatus) {
        this.customerStatus = customerStatus;
    }

    public CustomerType getCustomerType() {
        return customerType;
    }

    public void setCustomerType(CustomerType customerType) {
        this.customerType = customerType;
    }

    public String getCreditScore() {
        return creditScore;
    }

    public void setCreditScore(String creditScore) {
        this.creditScore = creditScore;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
