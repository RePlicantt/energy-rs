package com.replicantt.energyrs.repository;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

    public Request(String id, Long customerId, String type, String action) {
        this.id = id;
        this.customerId = customerId;
        this.type = type;
        this.action = action;
    }

    public Request() {
    }

    @Override
    public String toString() {
        return "Request{" +
                "id=" + id +
                ", customerId=" + customerId +
                ", type='" + type + '\'' +
                ", action='" + action + '\'' +
                '}';
    }
}
