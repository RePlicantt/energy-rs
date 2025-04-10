package com.replicantt.energyrs.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.replicantt.energyrs.repository.Customer;
import com.replicantt.energyrs.service.CustomerService;

public class CustomerController {

    private final CustomerService customerService;
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/customers")
    public List<Customer> findAll() {
        return customerService.getAllCustomers();
    }

    @PostMapping("/customers")
    public Customer addCustomer(Customer customer) {
        return customerService.addCustomer(customer);
    }

    @DeleteMapping("/customers/{id}")
    public void deleteCustomer(Long id) {
        customerService.deleteCustomer(id);
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
