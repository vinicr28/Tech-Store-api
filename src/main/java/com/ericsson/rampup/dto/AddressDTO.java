package com.ericsson.rampup.dto;

import com.ericsson.rampup.entities.Address;
import com.ericsson.rampup.entities.enums.AddressType;

import java.io.Serial;
import java.io.Serializable;

public class AddressDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private String street;
    private Integer houseNumber;
    private String neighborhood;
    private Integer zipCode;
    private String country;
    private AddressType addressType;

    private Long customerId;

    public AddressDTO() {
    }

    public AddressDTO(Address obj) {
        this.id = obj.getId();
        this.street = obj.getStreet();
        this.houseNumber = obj.getHouseNumber();
        this.neighborhood = obj.getNeighborhood();
        this.zipCode = obj.getZipCode();
        this.country = obj.getCountry();
        this.addressType = obj.getAddressType();
    }

    public AddressDTO(ViaCepDTO objDto) {
        this.street = objDto.getLogradouro();
        this.houseNumber = objDto.getNumero();
        this.neighborhood = objDto.getBairro();
        this.country = "Brasil";
        this.addressType = AddressType.HomeAddress;
        this.customerId = objDto.getCustomerId();
    }

    public Long getId() {
        return id;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public Integer getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(Integer houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

    public Integer getZipCode() {
        return zipCode;
    }

    public void setZipCode(Integer zipCode) {
        this.zipCode = zipCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public AddressType getAddressType() {
        return addressType;
    }

    public void setAddressType(AddressType addressType) {
        this.addressType = addressType;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }
}
