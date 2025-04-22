package com.replicantt.energyrs.repository;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "request_id_state")
@Getter
@Setter
public class RequestIdState {
    @Id
    private String id = "current_state";
    private char currentLetter;
    private int currentNumber;

}
