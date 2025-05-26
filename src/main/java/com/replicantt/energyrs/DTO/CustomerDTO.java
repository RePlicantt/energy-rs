package com.replicantt.energyrs.DTO;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerDTO {
    @NotBlank(message = "First name cannot be blank")
    private String firstName;
    private String middleName;
    @NotBlank(message = "Last name cannot be blank")
    private String lastName;
    @Email
    private String email;
    @NotNull(message = "Birth cannot be blank")
    @Past
    private LocalDate birth;
    @NotBlank(message = "Phone number cannot be blank")
    @Pattern(regexp = "^\\+?[1-9]\\d{7,14}$", message = "Invalid phone number format")
    private String phoneNumber;
    @NotBlank(message = "Country cannot be blank")
    private String country;
    private String state;
    @NotBlank(message = "City cannot be blank")
    private String city;
    @NotBlank(message = "Street cannot be blank")
    private String street;
    @NotBlank(message = "House number cannot be blank")
    private String houseNumber;
    private String apartment;
    @NotBlank(message = "Postal code cannot be blank")
    private String postalCode;
}
