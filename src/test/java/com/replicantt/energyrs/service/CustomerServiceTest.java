package com.replicantt.energyrs.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.replicantt.energyrs.DTO.CustomerDTO;
import com.replicantt.energyrs.repository.Customer;
import com.replicantt.energyrs.repository.CustomerRepository;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {
    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    @Test
    void testGetAllCustomers() {
        Customer customer = new Customer();
        Customer customer1 = new Customer();

        when(customerRepository.findAll()).thenReturn(List.of(customer, customer1));
        
        List<Customer> customers = customerService.getAllCustomers();
        
        verify(customerRepository).findAll();
        
        assertNotNull(customers);
        assertEquals(2, customers.size());
    }

    @Test
    void testGetCustomer() {
        Customer mockCustomer = new Customer();

        mockCustomer.setId(1L);
        mockCustomer.setFirstName("Robert");
        mockCustomer.setMiddleName("James");
        mockCustomer.setLastName("Clinton");
        mockCustomer.setEmail("robert@example.com");
        mockCustomer.setBirth(LocalDate.of(2001, 05, 12));
        mockCustomer.setPhoneNumber("+37199999999");
        mockCustomer.setCountry("USA");
        mockCustomer.setState("California");
        mockCustomer.setCity("Los Angeles");
        mockCustomer.setStreet("Sunset Blvd");
        mockCustomer.setHouseNumber("100");
        mockCustomer.setApartment("10A");
        mockCustomer.setPostalCode("90001");

        when(customerRepository.findById(mockCustomer.getId())).thenReturn(Optional.of(mockCustomer));

        Customer result = customerService.getCustomer(mockCustomer.getId());

        verify(customerRepository).findById(mockCustomer.getId());

        assertEquals(1L, result.getId());
        assertEquals("Robert", result.getFirstName());
        assertEquals("James", result.getMiddleName());
        assertEquals("Clinton", result.getLastName());
        assertEquals("robert@example.com", result.getEmail());
        assertEquals(LocalDate.of(2001, 05, 12), result.getBirth());
        assertEquals("+37199999999", result.getPhoneNumber());
        assertEquals("USA", result.getCountry());
        assertEquals("California", result.getState());
        assertEquals("Los Angeles", result.getCity());
        assertEquals("Sunset Blvd", result.getStreet());
        assertEquals("100", result.getHouseNumber());
        assertEquals("10A", result.getApartment());
        assertEquals("90001", result.getPostalCode());
    }

    @Test
    void testGetCustomer_withInvalidID () {
        Long invalidId = 0L;

        when(customerRepository.findById(invalidId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            customerService.getCustomer(invalidId);
        });
    }

    @Test
    void testAddCustomer() {
        CustomerDTO mockCustomerDTO = new CustomerDTO();

        mockCustomerDTO.setFirstName("Robert");
        mockCustomerDTO.setMiddleName("James");
        mockCustomerDTO.setLastName("Clinton");
        mockCustomerDTO.setEmail("robert@example.com");
        mockCustomerDTO.setBirth(LocalDate.of(2001, 05, 12));
        mockCustomerDTO.setPhoneNumber("+37199999999");
        mockCustomerDTO.setCountry("USA");
        mockCustomerDTO.setState("California");
        mockCustomerDTO.setCity("Los Angeles");
        mockCustomerDTO.setStreet("Sunset Blvd");
        mockCustomerDTO.setHouseNumber("100");
        mockCustomerDTO.setApartment("10A");
        mockCustomerDTO.setPostalCode("90001");

        when(customerRepository.save(any(Customer.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Customer result = customerService.addCustomer(mockCustomerDTO);

        assertEquals("Robert", result.getFirstName());
        assertEquals("James", result.getMiddleName());
        assertEquals("Clinton", result.getLastName());
        assertEquals("robert@example.com", result.getEmail());
        assertEquals(LocalDate.of(2001, 05, 12), result.getBirth());
        assertEquals("+37199999999", result.getPhoneNumber());
        assertEquals("USA", result.getCountry());
        assertEquals("California", result.getState());
        assertEquals("Los Angeles", result.getCity());
        assertEquals("Sunset Blvd", result.getStreet());
        assertEquals("100", result.getHouseNumber());
        assertEquals("10A", result.getApartment());
        assertEquals("90001", result.getPostalCode());

    }

    @Test 
    void testDeleteCustomer() {
        Long id = 1L;

        when(customerRepository.existsById(id)).thenReturn(true);
        customerService.deleteCustomer(id);
        verify(customerRepository).deleteById(id);
        
    }

    @Test
    void testDeleteCustomer_withInvalidId() {
        Long invalidId = 0L;

        when(customerRepository.existsById(invalidId)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> {
            customerService.deleteCustomer(invalidId);
        });
    }

    @Test
    void testUpdateCustomer() {
        Customer mockCustomer = new Customer();
        
        Long id = 1L;

        mockCustomer.setId(id);
        mockCustomer.setFirstName("Robert");
        mockCustomer.setMiddleName("James");
        mockCustomer.setLastName("Clinton");
        mockCustomer.setEmail("robert@example.com");
        mockCustomer.setBirth(LocalDate.of(2001, 05, 12));
        mockCustomer.setPhoneNumber("+37199999999");
        mockCustomer.setCountry("USA");
        mockCustomer.setState("California");
        mockCustomer.setCity("Los Angeles");
        mockCustomer.setStreet("Sunset Blvd");
        mockCustomer.setHouseNumber("100");
        mockCustomer.setApartment("10A");
        mockCustomer.setPostalCode("90001");

        String firstNameToUpdate = "Gordon";
        String middleNameToUpdate = "Alex";
        String lastNameToUpdate = "Smith";
        String emailToUpdate = "gordon@example.com";
        LocalDate birthDateToUpdate = LocalDate.of(2003, 01, 20);
        String phoneNumberToUpdate = "37100000000";
        String countryToUpdate = "Country";
        String stateToUpdate = "State";
        String cityToUpdate = "City";
        String streetToUpdate = "Different Street";
        String houseNumberToUpdate = "01-10";
        String apartmentToUpdate = "Apartment 1";
        String postalCodeToUpdate = "12345";

        when(customerRepository.findById(id)).thenReturn(Optional.of(mockCustomer));
        when(customerRepository.save(any(Customer.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Customer result = customerService.updateCustomer(
                id, 
                firstNameToUpdate,
                middleNameToUpdate,
                lastNameToUpdate,
                emailToUpdate,
                birthDateToUpdate, 
                phoneNumberToUpdate, 
                countryToUpdate,
                stateToUpdate,
                cityToUpdate,
                streetToUpdate,
                houseNumberToUpdate,
                apartmentToUpdate,
                postalCodeToUpdate
                );

        assertEquals(1L, result.getId());
        assertEquals(firstNameToUpdate, result.getFirstName());
        assertEquals(middleNameToUpdate, result.getMiddleName());
        assertEquals(lastNameToUpdate, result.getLastName());
        assertEquals(emailToUpdate, result.getEmail());
        assertEquals(birthDateToUpdate, result.getBirth());
        assertEquals(phoneNumberToUpdate, result.getPhoneNumber());
        assertEquals(countryToUpdate, result.getCountry());
        assertEquals(stateToUpdate, result.getState());
        assertEquals(cityToUpdate, result.getCity());
        assertEquals(streetToUpdate, result.getStreet());
        assertEquals(houseNumberToUpdate, result.getHouseNumber());
        assertEquals(apartmentToUpdate, result.getApartment());
        assertEquals(postalCodeToUpdate, result.getPostalCode());

        verify(customerRepository).findById(id);
        verify(customerRepository).save(result);
    }

    @Test
    void testUpdateCustomer_withInvalidId() {
        Long invalidId = 0L;

        String firstNameToUpdate = "Gordon";
        String middleNameToUpdate = "Alex";
        String lastNameToUpdate = "Smith";
        String emailToUpdate = "gordon@example.com";
        LocalDate birthDateToUpdate = LocalDate.of(2003, 01, 20);
        String phoneNumberToUpdate = "37100000000";
        String countryToUpdate = "Country";
        String stateToUpdate = "State";
        String cityToUpdate = "City";
        String streetToUpdate = "Different Street";
        String houseNumberToUpdate = "01-10";
        String apartmentToUpdate = "Apartment 1";
        String postalCodeToUpdate = "12345";

        when(customerRepository.findById(invalidId)).thenReturn(null);

        assertThrows(RuntimeException.class, () -> {
            customerService.updateCustomer(
                invalidId, 
                firstNameToUpdate,
                middleNameToUpdate,
                lastNameToUpdate,
                emailToUpdate,
                birthDateToUpdate, 
                phoneNumberToUpdate, 
                countryToUpdate,
                stateToUpdate,
                cityToUpdate,
                streetToUpdate,
                houseNumberToUpdate,
                apartmentToUpdate,
                postalCodeToUpdate
                );
        });
    }
}