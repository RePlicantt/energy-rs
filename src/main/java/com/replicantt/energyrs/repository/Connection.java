package com.replicantt.energyrs.repository;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "connections")
@Builder
@Getter
@Setter
public class Connection {
    @Id
    private String meterId;
    private String dependedRequestId;
    @Enumerated(EnumType.STRING)
    private EnumRequestType type;
    @Enumerated(EnumType.STRING)
    private EnumStatus status;
    private LocalDate connectionDate;

    public Connection(String dependedRequestId, String meterId, EnumRequestType type, EnumStatus status, LocalDate connectionDate) {
        this.dependedRequestId = dependedRequestId;
        this.meterId = meterId;
        this.type = type;
        this.status = status;
        this.connectionDate = connectionDate;
    }

    
    public Connection() {
    }
    
    
    public enum EnumRequestType {
        ELECTRICITY,
        GAS
    }
    
    public enum EnumStatus {
        ACTIVE,
        INACTIVE
    }
    @Override
    public String toString() {
        return "Connections{" +
                "dependedRequestId='" + dependedRequestId + '\'' +
                ", meterId=" + meterId +
                ", type='" + type + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
