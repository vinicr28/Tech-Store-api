package com.ericsson.rampup.dto;

import com.ericsson.rampup.entities.User;
import com.ericsson.rampup.resources.view.View;
import com.fasterxml.jackson.annotation.JsonView;

import java.io.Serial;
import java.io.Serializable;

public class UserDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @JsonView(View.Base.class)
    private Long id;
    @JsonView(View.Base.class)
    private String email;
    private String password;
    private Long customerId;

    public UserDTO(){
    }

    public UserDTO(User obj) {
        this.id = obj.getId();
        this.email = obj.getEmail();
        this.password = obj.getPassword();
        if(obj.getCustomer() != null) {
            this.customerId = obj.getCustomer().getId();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }
}
