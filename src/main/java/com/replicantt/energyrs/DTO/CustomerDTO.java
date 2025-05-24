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
    @NotBlank(message = "Name cannot be blank")
    private String name;
    @Email
    private String email;
    @NotNull(message = "Birth cannot be blank")
    @Past
    private LocalDate birth;
    @NotBlank(message = "Phone number cannot be blank")
    @Pattern(regexp = "^\\+?[1-9]\\d{7,14}$", message = "Invalid phone number format")
    private String phoneNumber;
    @NotBlank(message = "Address cannot be blank")
    private String address;
}
