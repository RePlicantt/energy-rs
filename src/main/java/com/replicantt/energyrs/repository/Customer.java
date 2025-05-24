package com.replicantt.energyrs.repository;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "customers")
@Builder
@Getter
@Setter
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birth;
    private String phoneNumber;
    private String address;

    // TODO разбить адрес на отдельные поля Country, State (если есть), City, Street, House Number, Apartment (если есть), Postal Code
    // и сделать миграцию БД.
    // Также разбить имя на First Name, Middle Name (если есть), Last Name и сделать миграцию БД.
    public Customer(Long id, String name, String email, LocalDate birth, String phoneNumber, String address) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.birth = birth;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }

    public Customer() {
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", birth=" + birth +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}

