package com.replicantt.energyrs.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.replicantt.energyrs.DTO.CustomerDTO;
import com.replicantt.energyrs.repository.Customer;
import com.replicantt.energyrs.service.CustomerService;

import jakarta.validation.Valid;

@RestController
public class CustomerController {
    private final CustomerService customerService;
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/customers")
    public List<Customer> findAll() {
        return customerService.getAllCustomers();
    }

    @GetMapping("/customers/{id}")
    public Customer getCustomer(@PathVariable Long id) {
        return customerService.getCustomer(id);
    }
 
    @PostMapping("/customers")
    public ResponseEntity<Object> addCustomer(@Valid @RequestBody CustomerDTO customerDTO) {
        customerService.addCustomer(customerDTO);
        return ResponseEntity.status(201).build();
    }

    @DeleteMapping("/customers/{id}")
    public ResponseEntity<Object> deleteCustomer(@PathVariable Long id) {
        try {
            customerService.deleteCustomer(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/customers/{id}")
    public void updateCustomer(
        @PathVariable Long id,
        @RequestParam(required = true) String name,
        @RequestParam(required = false) String email,
        @RequestParam(required = true) LocalDate birth,
        @RequestParam(required = true) String phoneNumber,
        @RequestParam(required = true) String address
    ) {
        customerService.updateCustomer(id, name, email, birth, phoneNumber, address);
    }
}
