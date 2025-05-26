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
        String firstName = "Robert";
        String middleName = "James";
        String lastName = "Clinton";
        String email = "robert@example.com";
        LocalDate birthDate = LocalDate.of(2003, 01, 20);
        String phoneNumber = "+37199999999";
        String country = "USA";
        String state = "NY";
        String city = "New York";
        String street = "Broadway";
        String houseNumber = "123";
        String apartment = "";
        String postalCode = "10001";

        mockCustomer.setFirstName(firstName);
        mockCustomer.setMiddleName(middleName);
        mockCustomer.setLastName(lastName);
        mockCustomer.setEmail(email);
        mockCustomer.setBirth(birthDate);
        mockCustomer.setPhoneNumber(phoneNumber);
        mockCustomer.setCountry(country);
        mockCustomer.setState(state);
        mockCustomer.setCity(city);
        mockCustomer.setStreet(street);
        mockCustomer.setHouseNumber(houseNumber);
        mockCustomer.setApartment(apartment);
        mockCustomer.setPostalCode(postalCode);

        when(customerService.getCustomer(id)).thenReturn(mockCustomer);

        mockMvc.perform(get("/customers/" + id)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.firstName").value(firstName))
            .andExpect(jsonPath("$.middleName").value(middleName))
            .andExpect(jsonPath("$.lastName").value(lastName))
            .andExpect(jsonPath("$.email").value(email))
            .andExpect(jsonPath("$.birth").value(birthDate.toString()))
            .andExpect(jsonPath("$.phoneNumber").value(phoneNumber))
            .andExpect(jsonPath("$.country").value(country))
            .andExpect(jsonPath("$.state").value(state))
            .andExpect(jsonPath("$.city").value(city))
            .andExpect(jsonPath("$.street").value(street))
            .andExpect(jsonPath("$.houseNumber").value(houseNumber))
            .andExpect(jsonPath("$.apartment").value(apartment))
            .andExpect(jsonPath("$.postalCode").value(postalCode));
    }

    @Test
    void testAddCustomer() throws Exception {
        String jsonBody = "{\"firstName\":\"Robert\", \"middleName\":\"James\", \"lastName\":\"Clinton\",\"email\":\"robert@example.com\","
                + "\"birth\":\"2003-01-20\",\"phoneNumber\":\"+37199999999\",\"country\":\"USA\",\"state\":\"NY\",\"city\":\"New York\","
                + "\"street\":\"Broadway\",\"houseNumber\":\"123\",\"apartment\":\"\",\"postalCode\":\"10001\"}";

        mockMvc.perform(post("/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody))
                .andExpect(status().isCreated());
    }

    @Test
    void testAddCustomer_withInvalidEmail() throws Exception {
        String jsonBody = "{\"firstName\":\"Robert\", \"middleName\":\"James\", \"lastName\":\"Clinton\",\"email\":\"INVALID_EMAIL\","
                + "\"birth\":\"2003-01-20\",\"phoneNumber\":\"+37199999999\",\"country\":\"USA\",\"state\":\"NY\",\"city\":\"New York\","
                + "\"street\":\"Broadway\",\"houseNumber\":\"123\",\"apartment\":\"\",\"postalCode\":\"10001\"}";

        mockMvc.perform(post("/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testAddCustomer_withInvalidPhone() throws Exception {
        String jsonBody = "{\"firstName\":\"Robert\", \"middleName\":\"James\", \"lastName\":\"Clinton\",\"email\":\"robert@example.com\","
                + "\"birth\":\"2003-01-20\",\"phoneNumber\":\"INVALID_PHONE\",\"country\":\"USA\",\"state\":\"NY\",\"city\":\"New York\","
                + "\"street\":\"Broadway\",\"houseNumber\":\"123\",\"apartment\":\"\",\"postalCode\":\"10001\"}";

        mockMvc.perform(post("/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testAddCustomer_withFutureBirthDate() throws Exception {
        String jsonBody = "{\"firstName\":\"Robert\", \"middleName\":\"James\", \"lastName\":\"Clinton\",\"email\":\"robert@example.com\","
                + "\"birth\":\"2999-01-20\",\"phoneNumber\":\"INVALID_PHONE\",\"country\":\"USA\",\"state\":\"NY\",\"city\":\"New York\","
                + "\"street\":\"Broadway\",\"houseNumber\":\"123\",\"apartment\":\"\",\"postalCode\":\"10001\"}";

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

        mockCustomer.setFirstName("Robert");
        mockCustomer.setMiddleName("");
        mockCustomer.setLastName("Clinton");
        mockCustomer.setEmail("robert@example.com");
        mockCustomer.setBirth(LocalDate.of(2001, 05, 12));
        mockCustomer.setPhoneNumber("+37199999999");
        mockCustomer.setCountry("USA");
        mockCustomer.setState("NY");
        mockCustomer.setCity("New York");
        mockCustomer.setStreet("Broadway");
        mockCustomer.setHouseNumber("123");
        mockCustomer.setApartment("");
        mockCustomer.setPostalCode("10001");


        Long id = 1L;
        String firstNameToUpdate = "Alex";
        String middleNameToUpdate = "Gordon";
        String lastNameToUpdate = "Smith";
        String emailToUpdate = "alex@example.com";
        LocalDate birthToUpdate = LocalDate.of(2000,01,01);
        String phoneNumberToUpdate = "+37100000000";
        String countryToUpdate = "Canada";
        String stateToUpdate = "ON";
        String cityToUpdate = "Toronto";
        String streetToUpdate = "Queen St";
        String houseNumberToUpdate = "456";
        String apartmentToUpdate = "10A";
        String postalCodeToUpdate = "M5H 2N2";

        mockMvc.perform(put("/customers/" + id)
                .param("firstName", firstNameToUpdate)
                .param("middleName", middleNameToUpdate)
                .param("lastName", lastNameToUpdate)
                .param("email", emailToUpdate)
                .param("birth", birthToUpdate.toString())
                .param("phoneNumber", phoneNumberToUpdate)
                .param("country", countryToUpdate)
                .param("state", stateToUpdate)
                .param("city", cityToUpdate)
                .param("street", streetToUpdate)
                .param("houseNumber", houseNumberToUpdate)
                .param("apartment", apartmentToUpdate)
                .param("postalCode", postalCodeToUpdate)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

                verify(customerService).updateCustomer(
                        id,
                        firstNameToUpdate,
                        middleNameToUpdate,
                        lastNameToUpdate,
                        emailToUpdate,
                        birthToUpdate,
                        phoneNumberToUpdate,
                        countryToUpdate,
                        stateToUpdate,
                        cityToUpdate,
                        streetToUpdate,
                        houseNumberToUpdate,
                        apartmentToUpdate,
                        postalCodeToUpdate
                );

    }

}