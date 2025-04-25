package com.replicantt.energyrs.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.replicantt.energyrs.repository.Connection;
import com.replicantt.energyrs.repository.Connection.EnumRequestType;
import com.replicantt.energyrs.repository.Connection.EnumStatus;
import com.replicantt.energyrs.service.ConnectionService;

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
    public Connection addConnection(@RequestBody Connection connection) {
        return connectionService.addConnection(connection);
    }

    @DeleteMapping("/connections/{meterId}")
    public void deleteConnection(@PathVariable String meterId) {
        connectionService.deleteConnection(meterId);
    }

    @PutMapping("/connections/{meterId}")
    public void updateConnection(
        @PathVariable String meterId,
        @RequestParam(required = false) String dependedRequestId,
        @RequestParam(required = false) EnumRequestType type,
        @RequestParam(required = false) EnumStatus status
        ) {
        connectionService.updateConnection(meterId, dependedRequestId, type, status);
        }

}
