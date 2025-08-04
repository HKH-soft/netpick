package com.hossein.spring_project.customer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public class CustomerJPADataAccessServiceTest {
    
    private CustomerJPADataAccessService underTest;
    private AutoCloseable autoCloseableMock;

    @Mock
    private CustomerRepository customerRepository;
    @BeforeEach
    void setUp(){
        autoCloseableMock = MockitoAnnotations.openMocks(this);
        underTest = new CustomerJPADataAccessService(customerRepository);
    }
    
    @AfterEach
    void tearDown() throws Exception {
        autoCloseableMock.close();
    }

    @Test
    void testCreateCustomer() {

        Customer customer = new Customer(
            "hossein","hossein@email.com","password",19, true);
        
        underTest.createCustomer(customer);

        verify(customerRepository).save(customer);
    }

    @Test
    void testExistsPersonWithEmail() {

        String email = "email";

        underTest.existsPersonWithEmail(email);

        verify(customerRepository).existsCustomerByEmail(email);


    }

    @Test
    void testExistsPersonWithId() {

        int id = 1;

        underTest.existsPersonWithId(id);

        verify(customerRepository).existsCustomerById(id);
        
    }

    @Test
    void testRemoveCustomer() {
        int id = 1;

        underTest.removeCustomer(id);

        verify(customerRepository).deleteById(id);

    }

    @Test
    void selectAllCustomersShouldReturnEmptyListWhenNoCustomers() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        Page<Customer> customerPage = Page.empty(pageable);
        
        // Mock the repository to return the empty page
        when(customerRepository.findAll(pageable)).thenReturn(customerPage);
        
        // Act
        List<Customer> result = underTest.selectAllCustomers(0, 10);
        
        // Assert
        assertThat(result).isEmpty();
        verify(customerRepository).findAll(pageable);
    }

    @Test
    void selectAllCustomersShouldReturnCustomersWhenTheyExist() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        List<Customer> customers = Arrays.asList(
            new Customer(1,"name","email1","password",21,true),
            new Customer(2,"name","email2","password",21,true)
        );
        Page<Customer> customerPage = new PageImpl<>(customers, pageable, customers.size());
        
        when(customerRepository.findAll(pageable)).thenReturn(customerPage);
        
        // Act
        List<Customer> result = underTest.selectAllCustomers(0, 10);
        
        // Assert
        assertThat(result).hasSize(2);
        assertThat(result).containsExactlyElementsOf(customers);
        verify(customerRepository).findAll(pageable);
    }

    @Test
    void testSelectCustomerById() {
        int id = 1;

        underTest.selectCustomerById(id);

        verify(customerRepository).findById(id);


    }

    @Test
    void testUpdateCustomer() {

        Customer customer = new Customer(
            "hossein","hossein@email.com","password",19, true);
        
        underTest.updateCustomer(customer);

        verify(customerRepository).save(customer);

    }
}
