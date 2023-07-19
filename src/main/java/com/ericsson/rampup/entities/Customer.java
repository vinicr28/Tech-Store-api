package com.ericsson.rampup.entities;

import com.ericsson.rampup.entities.enums.CustomerType;
import com.ericsson.rampup.resources.view.View;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "tb_customer")
@SQLDelete(sql = "UPDATE tb_customer SET deleted=1, customer_Status='deleted', customer_Name='deleted', " +
        "document_Number=00000000000, password='deleted',credit_Score='deleted' WHERE id=?")
public class Customer implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(View.Base.class)
    private Long id;
    @NotBlank
    @JsonView(View.Base.class)
    private String customerName;
    @NotNull
    @JsonView(View.Base.class)
    private Long documentNumber;

    @JsonView(View.Base.class)
    private String customerStatus;

    @JsonView(View.Base.class)
    private CustomerType customerType;
    @NotBlank
    @JsonView(View.Base.class)
    private String creditScore;
    private String password;
    private Boolean deleted = Boolean.FALSE;

    @JsonIgnoreProperties("customer")
    @OneToOne
    private User user;

    @JsonManagedReference
    @OneToMany(mappedBy = "customer", cascade = CascadeType.REMOVE)
    @LazyCollection(LazyCollectionOption.FALSE)
    @Where(clause = "deleted=false")
    @JsonView(View.Base.class)
    private List<Address> addresses = new ArrayList<>();

    @JsonIgnoreProperties("customer")
    @LazyCollection(LazyCollectionOption.FALSE) //simultaneously fetch multiple bags
    @OneToMany(mappedBy = "customer", cascade = CascadeType.REMOVE)
    @JsonView(View.Base.class)
    private List<Order> orders = new ArrayList<>();

    public Customer() {
    }

    public Customer(String customerName, Long documentNumber, String customerStatus, CustomerType customerType,
                    String creditScore) {
        this.customerName = customerName;
        this.documentNumber = documentNumber;
        this.customerStatus = customerStatus;
        this.customerType = customerType;
        this.creditScore = creditScore;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    public void addAddress(Address address){
        this.addresses.add(address);
    }

    public List<Order> getOrders() {
        return orders;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return Objects.equals(id, customer.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
