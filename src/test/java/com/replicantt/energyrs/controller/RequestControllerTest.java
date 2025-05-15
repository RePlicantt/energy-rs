package com.replicantt.energyrs.controller;

import org.springframework.http.MediaType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import java.time.LocalDateTime;

import com.replicantt.energyrs.repository.Request;
import com.replicantt.energyrs.repository.Request.EnumStatus;
import com.replicantt.energyrs.service.RequestService;

@ExtendWith(MockitoExtension.class)
class RequestControllerTest {

    @Mock
    private RequestService requestService;

    @InjectMocks
    private RequestController requestController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        // Инициализация MockMvc с контроллером и валидатором
        mockMvc = MockMvcBuilders
            .standaloneSetup(requestController)
            .setValidator(new LocalValidatorFactoryBean())
            .build();
    }
    
    @Test
    void testGetAllRequests() throws Exception {
        mockMvc.perform(get("/requests")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
    }
    
    @Test
    void testGetRequestById() throws Exception {
        Request mockRequest = new Request();
        String requestId = "RQA-000001";
        Long customerId = (long) 6;
        String type = "gas";
        String action = "shutdown";
        Request.EnumStatus status = Request.EnumStatus.SUBMITTED;
        LocalDateTime createdAt = LocalDateTime.of(2023, 10, 1, 12, 0, 0);
        
        mockRequest.setId(requestId);
        mockRequest.setCustomerId(customerId);
        mockRequest.setType(type);
        mockRequest.setAction(action);
        mockRequest.setStatus(status);
        mockRequest.setCreatedAt(createdAt);
        
        when(requestService.getRequestById(requestId)).thenReturn(mockRequest);
        
        mockMvc.perform(get("/requests/" + requestId)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(requestId))
            .andExpect(jsonPath("$.customerId").value(customerId))
            .andExpect(jsonPath("$.type").value(type))
            .andExpect(jsonPath("$.action").value(action))
            .andExpect(jsonPath("$.status").value(status.toString()))
            .andExpect(jsonPath("$.createdAt").value("2023-10-01T12:00"));
    }
    
    @Test
    void testAddRequest() throws Exception {
        String jsonBody = "{\"customerId\":6,\"type\":\"gas\",\"action\":\"shutdown\"}";

        mockMvc.perform(post("/requests")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody))
                .andExpect(status().isCreated());
    }

    @Test
    void testAddInvalidRequest() throws Exception {
        String jsonBody = "{\"customerId\":6,\"type\":null,\"action\":\"shu42tdown\"}";

        mockMvc.perform(post("/requests")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetInvalidRequestById() throws Exception {
        String invalidRequestId = "INVALID_ID";

        when(requestService.getRequestById(invalidRequestId)).thenReturn(null);

        mockMvc.perform(get("/requests/" + invalidRequestId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteRequest() throws Exception {
        String requestId = "RQA-000001";

        doNothing().when(requestService).deleteRequest(requestId);

        mockMvc.perform(delete("/requests/" + requestId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteRequestByInvalidId() throws Exception {
        String invalidRequestId = "INVALID_ID";

        doThrow(new RuntimeException("Request not found")).when(requestService).deleteRequest(invalidRequestId);

        mockMvc.perform(delete("/requests/" + invalidRequestId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateRequest() throws Exception {

        Request mockRequest = new Request();

        String requestId = "RQA-000001";
        String type = "electricity";
        String action = "connection";
        String status = "CANCELLED";

        when(requestService.getRequestById(requestId)).thenReturn(mockRequest);

        mockMvc.perform(put("/requests/" + requestId)
                .param("type", type)
                .param("action", action)
                .param("status", status)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(requestService).updateRequest(requestId, type, action, EnumStatus.CANCELLED);
    }

    @Test
    void testUpdateRequest_withInvalidRequestId() throws Exception {

        String requestId = "INVALID_REQUEST_ID";
        String type = "electricity";
        String action = "connection";
        String status = "CANCELLED";

        mockMvc.perform(put("/requests/" + requestId)
                .param("type", type)
                .param("action", action)
                .param("status", status)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

}
