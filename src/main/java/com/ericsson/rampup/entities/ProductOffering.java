package com.ericsson.rampup.entities;

import com.ericsson.rampup.entities.enums.PoState;
import com.ericsson.rampup.resources.view.View;
import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import javax.validation.constraints.*;

import com.fasterxml.jackson.annotation.JsonView;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "tb_productOffering")
@SQLDelete(sql = "UPDATE tb_product_offering SET deleted = 1 WHERE id=?")
@Where(clause = "deleted=false")
public class ProductOffering implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(View.Base.class)
    private Long id;
    @NotBlank
    @JsonView(View.Base.class)
    private String productName;
    @NotNull
    @Positive
    @DecimalMin(value = "1.0")
    @JsonView(View.Base.class)
    private Double unitPrice;
    @NotNull
    private Boolean sellIndicator;
    @NotNull
    @JsonView(View.Base.class)
    private PoState state;
    private Boolean deleted = Boolean.FALSE;

    @OneToMany(mappedBy = "id.productOffering", cascade = CascadeType.PERSIST)
    @LazyCollection(LazyCollectionOption.FALSE)
    private Set<OrderItem> items = new HashSet<>();

    public ProductOffering() {
    }

    public ProductOffering(String productName, Double unitPrice, Boolean sellIndicator, PoState state) {
        this.productName = productName;
        this.unitPrice = unitPrice;
        this.sellIndicator = sellIndicator;
        this.state = state;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Boolean isNotForSale() {
        return sellIndicator;
    }

    public void setSellIndicator(Boolean sellIndicator) {
        this.sellIndicator = sellIndicator;
    }

    public PoState getState() {
        return state;
    }

    public void setState(PoState state) {
        this.state = state;
    }

    @JsonIgnore
    public Set<Order> getOrders() {
        Set<Order> set = new HashSet<>();
        for(OrderItem orderItem : items){
            set.add(orderItem.getOder());
        }
        return set;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductOffering that = (ProductOffering) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
