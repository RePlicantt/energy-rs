package com.replicantt.energyrs.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.replicantt.energyrs.repository.Customer;
import com.replicantt.energyrs.repository.CustomerRepository;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }
    
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Customer addCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    public void deleteCustomer(Long id) {
        customerRepository.deleteById(id);
    }
    public void updateCustomer(Long id, String name, String email, LocalDate birth, String phoneNumber, String address) {
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new RuntimeException("Customer not found"));
        
        customer.setName(name);
        customer.setEmail(email);
        customer.setBirth(birth);
        customer.setPhoneNumber(phoneNumber);
        customer.setAddress(address);

        customerRepository.save(customer);
    }
}
