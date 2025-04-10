package com.replicantt.energyrs.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestRepository extends JpaRepository<Request, Long> {
    Optional<Request> findById(String id);
}
