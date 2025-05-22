package com.replicantt.energyrs.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.replicantt.energyrs.repository.Customer;
import com.replicantt.energyrs.repository.CustomerRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CustomerService {
    private final CustomerRepository customerRepository;
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }
    
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }
    public Customer getCustomer(Long id) {
        return customerRepository.findById(id).orElseThrow(() -> new RuntimeException("Customer not found"));
    }

    public Customer addCustomer(Customer customer) {
        log.debug("Customer saved: {}", customer);
        return customerRepository.save(customer);
    }

    public void deleteCustomer(Long id) {
        if (!customerRepository.existsById(id)) {
            throw new RuntimeException("Customer not found with ID: " + id);
        } else { 
            customerRepository.deleteById(id);
        }
    }
    public Customer updateCustomer(Long id, String name, String email, LocalDate birth, String phoneNumber, String address) {
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new RuntimeException("Customer not found"));
        
        customer.setName(name);
        customer.setEmail(email);
        customer.setBirth(birth);
        customer.setPhoneNumber(phoneNumber);
        customer.setAddress(address);

        return customerRepository.save(customer);
    }
}
