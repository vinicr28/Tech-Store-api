package com.ericsson.rampup.dto;

import com.ericsson.rampup.entities.Order;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class OrderDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private Long customerId;
    private Long addressId;
    private List<OrderItemDTO> orderItem = new ArrayList<>();
    private Double discount;

    public OrderDTO() {
    }

    public OrderDTO(Order obj) {
        this.id = obj.getId();
    }

    public Long getId() {
        return id;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    public List<OrderItemDTO> getOrderItem() {
        return orderItem;
    }
    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }
}
