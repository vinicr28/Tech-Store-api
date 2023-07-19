package com.ericsson.rampup.entities.pk;

import com.ericsson.rampup.entities.Order;
import com.ericsson.rampup.entities.ProductOffering;
import com.ericsson.rampup.resources.view.View;
import com.fasterxml.jackson.annotation.JsonView;

import javax.persistence.*;
import javax.persistence.*;
import javax.persistence.*;
import javax.persistence.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class OrderItemPK implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "product_offering_id")
    @JsonView(View.Base.class)
    private ProductOffering productOffering;

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public ProductOffering getProductOffering() {
        return productOffering;
    }

    public void setProductOffering(ProductOffering productOffering) {
        this.productOffering = productOffering;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderItemPK that = (OrderItemPK) o;
        return Objects.equals(order, that.order) && Objects.equals(productOffering, that.productOffering);
    }

    @Override
    public int hashCode() {
        return Objects.hash(order, productOffering);
    }
}
