package com.replicantt.energyrs.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RequestDTO {
    private String id; // ID запроса, генерируется автоматически
    @NotNull(message = "Customer ID cannot be blank")
    private Long customerId;
    @NotBlank(message = "Type cannot be blank")
    private String type;
    @NotBlank(message = "Action cannot be blank")
    private String action;
    private String status; // статус может быть null, если не передан, то устанавливаем по умолчанию SUBMITTED
    private String createdAt; // дата создания запроса, если не передана, то устанавливаем по умолчанию текущую дату и время
}
