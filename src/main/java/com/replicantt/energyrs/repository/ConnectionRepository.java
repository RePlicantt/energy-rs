package com.replicantt.energyrs.repository;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ConnectionRepository extends JpaRepository<Connection, String> {
    Connection findByMeterId(String meterId);
    Connection findByDependedRequestId(String dependedRequestId);
    Connection findAllByStatus(String status);
    Connection findAllByType(String type);
    Connection findByTypeAndStatus(String type, String status);
    Long deleteByMeterId(String meterId);
    boolean existsByMeterId(String meterId);

}
