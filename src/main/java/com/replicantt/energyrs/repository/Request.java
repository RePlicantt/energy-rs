package com.replicantt.energyrs.repository;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "requests")

@Getter
@Setter
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Request {
    @Id
    private String id;

    @Column(name = "customer_id")
    private Long customerId;
    
    private String type;
    private String action;
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "status_enum", nullable = false)
    private EnumStatus status;
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public Request(String id, Long customerId, String type, String action, EnumStatus status, LocalDateTime createdAt) {
        this.id = id;
        this.customerId = customerId;
        this.type = type;
        this.action = action;
        this.status = status != null ? status : EnumStatus.SUBMITTED;
        this.createdAt = createdAt;
    }

    public Request() {
        this.status = EnumStatus.SUBMITTED;
    }

    public enum EnumStatus {
        SUBMITTED,
        IN_PROGRESS,
        COMPLETED,
        REJECTED,
        CANCELLED;
    }

    @Override
    public String toString() {
        return "Request{" +
                "id='" + id + '\'' +
                ", customerId=" + customerId +
                ", type='" + type + '\'' +
                ", action='" + action + '\'' +
                ", status=" + status +
                ", createdAt=" + createdAt +
                '}';
    }
}
