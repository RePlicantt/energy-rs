package com.replicantt.energyrs.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.replicantt.energyrs.repository.Connection;
import com.replicantt.energyrs.repository.Connection.EnumRequestType;
import com.replicantt.energyrs.repository.Connection.EnumStatus;

import jakarta.transaction.Transactional;

import com.replicantt.energyrs.repository.ConnectionRepository;

@Service
public class ConnectionService {
    private final ConnectionRepository connectionRepository;
    public ConnectionService(ConnectionRepository connectionRepository) {
        this.connectionRepository = connectionRepository;
    }

    public Connection getConnection(String meterId) {
        return connectionRepository.findByMeterId(meterId);
    }
    
    public List<Connection> getAllConnections() {
        return connectionRepository.findAll();
    }
    @Transactional
    public Connection addConnection(Connection connection) {
        if (connectionRepository.existsByMeterId(connection.getMeterId())) {
            throw new RuntimeException("Connection with this meterId already exists");
        }
        System.out.println("Saving connection: " + connection);
        return connectionRepository.save(connection);
    }

    @Transactional
    public void deleteConnection(String meterId) {
        connectionRepository.deleteByMeterId(meterId);
    }

    @Transactional
    public void updateConnection(String meterId, String dependedRequestId, EnumRequestType type, EnumStatus status) {
        Connection connection = connectionRepository.findByMeterId(meterId);
        if (connection == null) {
            throw new RuntimeException("Connection not found");
        }

        if (dependedRequestId != null) {
            connection.setDependedRequestId(dependedRequestId);
        }
        if (status != null) {
            connection.setStatus(status);
        }
        if (type != null) {
            connection.setType(type);
        }

        connectionRepository.save(connection);
    }
}
