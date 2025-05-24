package com.replicantt.energyrs.controller;

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

import java.time.LocalDate;

import com.replicantt.energyrs.repository.Customer;
import com.replicantt.energyrs.service.CustomerService;

@ExtendWith(MockitoExtension.class)
public class CustomerControllerTest {
    @Mock
    private CustomerService customerService;

    @InjectMocks
    private CustomerController customerController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        
        mockMvc = MockMvcBuilders
            .standaloneSetup(customerController)
            .setValidator(new LocalValidatorFactoryBean())
            .build();
    }

    @Test
    void testGetAllCustomers() throws Exception {
        mockMvc.perform(get("/customers")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
    }

    @Test
    void testGetCustomer() throws Exception {
        Customer mockCustomer = new Customer();

        Long id = 1L;
        String name = "Gordon";
        String email = "gordon@example.com";
        LocalDate birthDate = LocalDate.of(2003, 01, 20);
        String phoneNumber = "+37100000000";
        String address = "Country, City, Different Street st. 01-10";

        mockCustomer.setId(id);
        mockCustomer.setName(name);
        mockCustomer.setEmail(email);
        mockCustomer.setBirth(birthDate);
        mockCustomer.setPhoneNumber(phoneNumber);
        mockCustomer.setAddress(address);

        when(customerService.getCustomer(id)).thenReturn(mockCustomer);

        mockMvc.perform(get("/customers/" + id)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value(name))
            .andExpect(jsonPath("$.email").value(email))
            .andExpect(jsonPath("$.birth").value(birthDate.toString()))
            .andExpect(jsonPath("$.phoneNumber").value(phoneNumber))
            .andExpect(jsonPath("$.address").value(address));
    }

    @Test
    void testAddCustomer() throws Exception {
        String jsonBody = "{\"name\":\"Gordon\",\"email\":\"gordon@example.com\","
                + "\"birth\":\"2003-01-20\",\"phoneNumber\":\"+37100000000\",\"address\":\"Country, City, Different Street st. 01-10\"}";

        mockMvc.perform(post("/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody))
                .andExpect(status().isCreated());
    }

    @Test
    void testAddCustomer_withInvalidEmail() throws Exception {
        String jsonBody = "{\"name\":\"Gordon\",\"email\":\"INVALID_EMAIL\","
                + "\"birth\":\"2003-01-20\",\"phoneNumber\":\"+37100000000\",\"address\":\"Country, City, Different Street st. 01-10\"}";

        mockMvc.perform(post("/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testAddCustomer_withInvalidPhone() throws Exception {
        String jsonBody = "{\"name\":\"Gordon\",\"email\":\"gordon@example.com\","
                + "\"birth\":\"2003-01-20\",\"phoneNumber\":\"INVALID_PHONE\",\"address\":\"Country, City, Different Street st. 01-10\"}";

        mockMvc.perform(post("/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testAddCustomer_withFutureBirthDate() throws Exception {
        String jsonBody = "{\"name\":\"Gordon\",\"email\":\"gordon@example.com\","
                + "\"birth\":\"2999-01-20\",\"phoneNumber\":\"+37100000000\",\"address\":\"Country, City, Different Street st. 01-10\"}";

        mockMvc.perform(post("/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testDeleteCustomer() throws Exception {
        Long id = 1L;

        doNothing().when(customerService).deleteCustomer(id);

        mockMvc.perform(delete("/customers/" + id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteCustomer_withNonExistingId() throws Exception {
        Long id = 0L;

        doThrow(new RuntimeException("Connection not found"))
                .when(customerService).deleteCustomer(id);

        mockMvc.perform(delete("/customers/" + id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateCustomer() throws Exception {
        Customer mockCustomer = new Customer();

        Long id = 1L;

        mockCustomer.setName("Alex");
        mockCustomer.setEmail("alex@example.com");
        mockCustomer.setBirth(LocalDate.of(2001, 05, 12));
        mockCustomer.setPhoneNumber("+37199999999");
        mockCustomer.setAddress("Country, City, Street st. 99-99");

        String nameToUpdate = "Gordon";
        String emailToUpdate = "gordon@example.com";
        LocalDate birthToUpdate = LocalDate.of(2000,01,01);
        String phoneNumberToUpdate = "+37100000000";
        String addressToUpdate = "Country, City, Street st. 00-00";

        mockMvc.perform(put("/customers/" + id)
                .param("name", nameToUpdate)
                .param("email", emailToUpdate)
                .param("birth", birthToUpdate.toString())
                .param("phoneNumber", phoneNumberToUpdate)
                .param("address", addressToUpdate)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

                verify(customerService).updateCustomer(
                        id,
                        nameToUpdate,
                        emailToUpdate,
                        birthToUpdate,
                        phoneNumberToUpdate,
                        addressToUpdate
                );

    }

}