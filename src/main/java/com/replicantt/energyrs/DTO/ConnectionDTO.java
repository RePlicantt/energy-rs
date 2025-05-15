package com.replicantt.energyrs.DTO;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ConnectionDTO {
    @NotNull(message = "Meter ID cannot be blank")
    @Pattern(regexp = "^\\d{9}$", message = "Meter ID must be in the format 000000000")
    private String meterId;
    @NotNull(message = "Meter ID cannot be blank")
    @Pattern(regexp = "^RQ[A-Z]-\\d{6}$", message = "Request ID must be in the format 000000000")
    private String dependedRequestId;
    @NotNull(message = "Meter ID cannot be blank")
    @Pattern(regexp = "ELECTRICITY|GAS", message = "Type must be either ELECTRICITY or GAS")
    private String type;
    @NotNull(message = "Meter ID cannot be blank")
    @Pattern(regexp = "ACTIVE|INACTIVE", message = "Status must be either ACTIVE or INACTIVE")
    private String status;
    private LocalDate connectionDate;

}
