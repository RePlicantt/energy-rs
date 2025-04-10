package com.replicantt.energyrs.repository;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    // Custom query methods can be defined here if needed
    // For example, findByEmail, findByName, etc.
}
