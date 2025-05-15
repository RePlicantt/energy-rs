package com.replicantt.energyrs.controller;

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

import com.replicantt.energyrs.DTO.ConnectionDTO;
import com.replicantt.energyrs.repository.Connection;
import com.replicantt.energyrs.repository.Connection.EnumRequestType;
import com.replicantt.energyrs.repository.Connection.EnumStatus;
import com.replicantt.energyrs.service.ConnectionService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class ConnectionController {
    private final ConnectionService connectionService;

    public ConnectionController(ConnectionService connectionService) {
        this.connectionService = connectionService;
    }

    @GetMapping("/connections/{meterId}")
    public Connection getConnection(@PathVariable String meterId) {
        if (meterId == null) {
            throw new IllegalArgumentException("Meter ID is required");
        }
        return connectionService.getConnection(meterId);
    }

    @GetMapping("/connections")
    public List<Connection> getAllConnections() {
        return connectionService.getAllConnections();
    }

    @PostMapping("/connections")
    public ResponseEntity<Object> addConnection(@Valid @RequestBody ConnectionDTO connectionDTO) {
        connectionService.addConnection(connectionDTO);
        return ResponseEntity.status(201).build();
    }

    @DeleteMapping("/connections/{meterId}")
    public ResponseEntity<Void> deleteConnection(@PathVariable String meterId) {
        try {
            connectionService.deleteConnection(meterId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            log.warn("Connection not found for meterId: {}", meterId);
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/connections/{meterId}")
    public ResponseEntity<Void> updateConnection(
        @PathVariable String meterId,
        @RequestParam(required = false) String dependedRequestId,
        @RequestParam(required = false) EnumRequestType type,
        @RequestParam(required = false) EnumStatus status
        ) {
        Connection connection = connectionService.getConnection(meterId);
        if (connection != null){
            connectionService.updateConnection(meterId, dependedRequestId, type, status);
            return ResponseEntity.ok().build();
        } else {
            log.warn("Connection not found for meterId: {}", meterId);
            return ResponseEntity.notFound().build();
        }
    }

}
