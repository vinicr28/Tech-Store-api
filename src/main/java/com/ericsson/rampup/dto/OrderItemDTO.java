package com.ericsson.rampup.dto;

import java.io.Serial;
import java.io.Serializable;

public class OrderItemDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long productId;
    private Integer quantity;

    public OrderItemDTO() {
    }

    public Long getId() {
        return productId;
    }

    public void setId(Long id) {
        this.productId = id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
