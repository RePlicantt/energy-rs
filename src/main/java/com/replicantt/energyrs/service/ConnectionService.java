package com.replicantt.energyrs.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.replicantt.energyrs.DTO.ConnectionDTO;
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
        Connection connection = connectionRepository.findByMeterId(meterId);
        if (connection == null) {
            throw new RuntimeException("Connection not found");
        }
        return connection;
    }
    
    public List<Connection> getAllConnections() {
        return connectionRepository.findAll();
    }
    @Transactional
    public Connection addConnection(ConnectionDTO connectionDTO) {
        if (connectionRepository.existsByMeterId(connectionDTO.getMeterId())) {
            throw new RuntimeException("Connection with this meterId already exists");
        }
        
        System.out.println("Saving connection: " + connectionDTO);
        Connection connectionToSave = Connection.builder()
            .meterId(connectionDTO.getMeterId())
            .type(EnumRequestType.valueOf(connectionDTO.getType()))
            .status(EnumStatus.valueOf(connectionDTO.getStatus()))
            .dependedRequestId(connectionDTO.getDependedRequestId())
            .build();

        return connectionRepository.save(connectionToSave);
    }

    public void deleteConnection(String meterId) {
        if (connectionRepository.existsByMeterId(meterId)) {
            connectionRepository.deleteByMeterId(meterId);
        } else {
            throw new RuntimeException("Connection not found with meter: " + meterId);
        }
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
