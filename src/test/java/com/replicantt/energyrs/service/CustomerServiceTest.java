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
        mockCustomer.setName("Alex");
        mockCustomer.setEmail("alex@example.com");
        mockCustomer.setBirth(LocalDate.of(2001, 05, 12));
        mockCustomer.setPhoneNumber("+37199999999");
        mockCustomer.setAddress("Country, City, Street st. 99-99");

        when(customerRepository.findById(mockCustomer.getId())).thenReturn(Optional.of(mockCustomer));

        Customer result = customerService.getCustomer(mockCustomer.getId());

        verify(customerRepository).findById(mockCustomer.getId());

        assertEquals(1L, result.getId());
        assertEquals("Alex", result.getName());
        assertEquals("alex@example.com", result.getEmail());
        assertEquals(LocalDate.of(2001, 05, 12), result.getBirth());
        assertEquals("+37199999999", result.getPhoneNumber());
        assertEquals("Country, City, Street st. 99-99", result.getAddress());
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

        mockCustomerDTO.setName("Alex");
        mockCustomerDTO.setEmail("alex@example.com");
        mockCustomerDTO.setBirth(LocalDate.of(2001, 05, 12));
        mockCustomerDTO.setPhoneNumber("+37199999999");
        mockCustomerDTO.setAddress("Country, City, Street st. 99-99");

        when(customerRepository.save(any(Customer.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Customer result = customerService.addCustomer(mockCustomerDTO);

        assertEquals("Alex", result.getName());
        assertEquals("alex@example.com", result.getEmail());
        assertEquals(LocalDate.of(2001, 05, 12), result.getBirth());
        assertEquals("+37199999999", result.getPhoneNumber());
        assertEquals("Country, City, Street st. 99-99", result.getAddress());

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
        mockCustomer.setName("Alex");
        mockCustomer.setEmail("alex@example.com");
        mockCustomer.setBirth(LocalDate.of(2001, 05, 12));
        mockCustomer.setPhoneNumber("+37199999999");
        mockCustomer.setAddress("Country, City, Street st. 99-99");

        String nameToUpdate = "Gordon";
        String emailToUpdate = "gordon@example.com";
        LocalDate birthDateToUpdate = LocalDate.of(2003, 01, 20);
        String phoneNumberToUpdate = "37100000000";
        String adressToUpdate = "Country, City, Different Street st. 01-10";

        when(customerRepository.findById(id)).thenReturn(Optional.of(mockCustomer));
        when(customerRepository.save(any(Customer.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Customer result = customerService.updateCustomer(id, nameToUpdate, emailToUpdate, birthDateToUpdate, phoneNumberToUpdate, adressToUpdate);

        assertEquals(1L, result.getId());
        assertEquals(nameToUpdate, result.getName());
        assertEquals(emailToUpdate, result.getEmail());
        assertEquals(birthDateToUpdate, result.getBirth());
        assertEquals(phoneNumberToUpdate, result.getPhoneNumber());
        assertEquals(adressToUpdate, result.getAddress());
    }

    @Test
    void testUpdateCustomer_withInvalidId() {
        Long invalidId = 0L;

        String nameToUpdate = "Gordon";
        String emailToUpdate = "gordon@example.com";
        LocalDate birthDateToUpdate = LocalDate.of(2003, 01, 20);
        String phoneNumberToUpdate = "37100000000";
        String adressToUpdate = "Country, City, Different Street st. 01-10";

        when(customerRepository.findById(invalidId)).thenReturn(null);

        assertThrows(RuntimeException.class, () -> {
            customerService.updateCustomer(invalidId, nameToUpdate, emailToUpdate, birthDateToUpdate, phoneNumberToUpdate, adressToUpdate);
        });
    }
}