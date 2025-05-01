package com.replicantt.controller;

import org.springframework.http.MediaType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.replicantt.energyrs.controller.RequestController;
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
        // Initialize MockMvc with the RequestController
        mockMvc = MockMvcBuilders.standaloneSetup(requestController).build();
    }

    @Test
    void testCreateRequest() throws Exception {
        String jsonBody = "{\"customerId\":6,\"type\":\"gas\",\"action\":\"shutdown\"}";

        mockMvc.perform(post("/requests")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody))
                .andExpect(status().isCreated());
    }

}
