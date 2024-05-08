package com.pulsar.payload.request;

import com.pulsar.models.AddressType;
import jakarta.validation.constraints.NotBlank;

public class ProfileRequest {
    @NotBlank
    private String streetAddress;

    @NotBlank
    private String city;

    @NotBlank
    private String postalCode;

    private AddressType addressType;


    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public AddressType getAddressType() {
        return addressType;
    }

    public void setAddressType(AddressType addressType) {
        this.addressType = addressType;
    }
}
