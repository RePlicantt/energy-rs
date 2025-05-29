package com.replicantt.energyrs.repository;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "customers")
@Builder
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String middleName;
    private String lastName;

    private String email;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birth;

    private String phoneNumber;

    private String country;
    private String state;
    private String city;
    private String street;
    private String houseNumber;
    private String apartment;
    private String postalCode;
    private String fullAddress;

    public Customer(
            Long id, 
            String firstName, 
            String middleName, 
            String lastName, 
            String email, 
            LocalDate birth, 
            String phoneNumber, 
            String country,
            String state,
            String city,
            String street,
            String houseNumber,
            String apartment,
            String postalCode
        ) {

        this.id = id;

        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;

        this.email = email;

        this.birth = birth;

        this.phoneNumber = phoneNumber;

        this.country = country;
        this.state = state;
        this.city = city;
        this.street = street;
        this.houseNumber = houseNumber;
        this.apartment = apartment;
        this.postalCode = postalCode;
        this.fullAddress = country + ", " + 
                            state + ", " + 
                            city + ", " + 
                            street + " " + 
                            houseNumber + ", " + 
                            apartment + ", " + 
                            postalCode;
    }

    public Customer() {
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", middleName='" + middleName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", birth=" + birth +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", country='" + country + '\'' +
                ", state='" + state + '\'' +
                ", city='" + city + '\'' +
                ", street='" + street + '\'' +
                ", houseNumber='" + houseNumber + '\'' +
                ", apartment='" + apartment + '\'' +
                ", postalCode='" + postalCode + '\'' +
                '}';
    }
}

