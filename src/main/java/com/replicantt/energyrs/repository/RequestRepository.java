package com.replicantt.energyrequestsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestRepository extends JpaRepository<Request, Long> {
    // Custom query methods can be defined here if needed
    // For example, findByCustomerId, findByType, etc.

}
