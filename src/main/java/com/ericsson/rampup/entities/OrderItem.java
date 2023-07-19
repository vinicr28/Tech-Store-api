package com.ericsson.rampup.entities;

import com.ericsson.rampup.entities.pk.OrderItemPK;
import com.ericsson.rampup.resources.view.View;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;

import javax.persistence.*;
import javax.persistence.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "tb_orderItem")
public class OrderItem implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    @JsonView(View.Base.class)
    private OrderItemPK id = new OrderItemPK();
    @JsonView(View.Base.class)
    private Integer quantity;
    @JsonView(View.Base.class)
    private Double discount;
    @JsonView(View.Base.class)
    private Double totalPrice;

    public OrderItem() {
    }

    public OrderItem(Order order, ProductOffering productOffering, Integer quantity, Double discount) {
        id.setOrder(order);
        id.setProductOffering(productOffering);
        this.quantity = quantity;
        this.discount = discount;
        this.setTotalPrice(productOffering);
    }

    @JsonIgnore
    public Order getOder(){
        return id.getOrder();
    }

    public void setOrder(Order order){
        id.setOrder(order);
    }

    public ProductOffering getProductOffering(){
        return id.getProductOffering();
    }

    public void setProductOffering(ProductOffering productOffering){
        id.setProductOffering(productOffering);
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(ProductOffering productOffering) {
        this.totalPrice = (productOffering.getUnitPrice() * quantity) - discount;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderItem orderItem = (OrderItem) o;
        return Objects.equals(id, orderItem.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
