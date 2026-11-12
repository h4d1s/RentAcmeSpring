package com.pixellabs.user.domain.userAggregate;

import com.pixellabs.user.domain.userAggregate.exceptions.UserDomainException;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Embeddable
public class Address {
    private String street;
    private String city;
    private String postalCode;
    private String country;

    public Address(
        String street,
        String city,
        String postalCode,
        String country) throws UserDomainException {
        updateStreet(street);
        updateCity(city);
        updatePostalCode(postalCode);
        updateCountry(country);
    }

    public void updateStreet(String street) throws UserDomainException {
        validateStreet(street);
        this.street = street;
    }

    public void updateCity(String city) throws UserDomainException {
        validateCity(city);
        this.city = city;
    }

    public void updatePostalCode(String postalCode) throws UserDomainException {
        validatePostalCode(postalCode);
        this.postalCode = postalCode;
    }

    public void updateCountry(String country) throws UserDomainException {
        validateCountry(country);
        this.country = country;
    }

    private void validateStreet(String street) throws UserDomainException {
        if (street == null || street.isBlank()) {
            throw new UserDomainException("Street cannot be empty.");
        }
    }

    private void validateCity(String city) throws UserDomainException {
        if (city == null || city.isBlank()) {
            throw new UserDomainException("City cannot be empty.");
        }
    }

    private void validatePostalCode(String postalCode) throws UserDomainException {
        if (postalCode == null || postalCode.length() < 5) {
            throw new UserDomainException("Invalid Postal Code.");
        }
    }

    private void validateCountry(String country) throws UserDomainException {
        if (country == null || country.isBlank()) {
            throw new UserDomainException("Country cannot be empty.");
        }
    }
}
