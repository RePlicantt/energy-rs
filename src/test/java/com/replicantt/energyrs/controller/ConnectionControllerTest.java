package com.replicantt.energyrs.controller;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import com.replicantt.energyrs.repository.Connection;
import com.replicantt.energyrs.repository.Connection.EnumRequestType;
import com.replicantt.energyrs.repository.Connection.EnumStatus;
import com.replicantt.energyrs.service.ConnectionService;

@ExtendWith(MockitoExtension.class)
public class ConnectionControllerTest {

    @Mock
    private ConnectionService connectionService;

    @InjectMocks
    private ConnectionController connectionController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        
        mockMvc = MockMvcBuilders
            .standaloneSetup(connectionController)
            .setValidator(new LocalValidatorFactoryBean())
            .build();
    }

    @Test
    void testGetAllConnections() throws Exception {
        mockMvc.perform(get("/connections")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
    }

    @Test
    void testGetConnection() throws Exception {
        Connection mockConnection = new Connection();
        String meterId = "000000001";
        String dependedRequestId = "RQA-000001";
        EnumRequestType type = EnumRequestType.GAS;
        Connection.EnumStatus status = Connection.EnumStatus.ACTIVE;
        mockConnection.setMeterId(meterId);
        mockConnection.setDependedRequestId(dependedRequestId);
        mockConnection.setType(type);
        mockConnection.setStatus(status);

        when(connectionService.getConnection(meterId)).thenReturn(mockConnection);
        mockMvc.perform(get("/connections/" + meterId)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.meterId").value(meterId))
            .andExpect(jsonPath("$.dependedRequestId").value(dependedRequestId))
            .andExpect(jsonPath("$.type").value(type.toString()))
            .andExpect(jsonPath("$.status").value(status.toString()));
    }

    @Test
    void testAddConnection() throws Exception {
        String jsonBody = "{\"meterId\":\"000000001\",\"dependedRequestId\":\"RQA-000006\","
                + "\"type\":\"GAS\",\"status\":\"ACTIVE\"}";

        mockMvc.perform(post("/connections")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody))
                .andExpect(status().isCreated());
    }

    @Test
    void testInvalidAddConnection() throws Exception {
        String jsonBody = "{\"meterId\":\"000000001\",\"INVALID_TYPE\":\"RQA-000006\","
                + "\"type\":\"GAS\",\"status\":\"ACTIVE\"}";

        mockMvc.perform(post("/connections")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testDeleteConnection() throws Exception {
        String meterId = "000000001";

        doNothing().when(connectionService).deleteConnection(meterId);

        mockMvc.perform(delete("/connections/" + meterId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void testInvalidDeleteConnection() throws Exception {
        String meterId = "INVALID_METER_ID";

        doThrow(new RuntimeException("Connection not found"))
                .when(connectionService).deleteConnection(meterId);

        mockMvc.perform(delete("/connections/" + meterId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }


    @Test
    void testUpdateConnection() throws Exception {
        Connection mockConnection = new Connection();

        String meterId = "000000001";
        String dependedRequestId = "RQA-000001";
        EnumRequestType type = EnumRequestType.ELECTRICITY;
        EnumStatus status = EnumStatus.ACTIVE;

        when(connectionService.getConnection(meterId)).thenReturn(mockConnection);

        mockMvc.perform(put("/connections/" + meterId)
                .param("dependedRequestId", dependedRequestId)
                .param("type", type.toString())
                .param("status", status.toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(connectionService).updateConnection(
                meterId, 
                dependedRequestId, 
                EnumRequestType.ELECTRICITY, 
                EnumStatus.ACTIVE
        );
    }

    @Test
    void testUpdateConnection_withInvalidMeterId() throws Exception {
        String meterId = "INVALID_METER_ID";
        String dependedRequestId = "RQA-000001";
        EnumRequestType type = EnumRequestType.ELECTRICITY;
        EnumStatus status = EnumStatus.ACTIVE;

        when(connectionService.getConnection(meterId)).thenReturn(null);

        mockMvc.perform(put("/connections/" + meterId)
                .param("dependedRequestId", dependedRequestId)
                .param("type", type.toString())
                .param("status", status.toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

}
