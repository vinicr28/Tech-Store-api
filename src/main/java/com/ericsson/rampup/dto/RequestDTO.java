package com.ericsson.rampup.dto;

import java.io.Serial;
import java.io.Serializable;

public class RequestDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long orderId;

    public RequestDTO() {
    }

    public RequestDTO(Long orderId) {
        this.orderId = orderId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }
}
