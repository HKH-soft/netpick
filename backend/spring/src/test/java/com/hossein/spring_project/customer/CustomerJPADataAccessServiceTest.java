package com.hossein.spring_project.customer;

import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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
            "hossein","hossein@email.com",19,true);
        
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
    void testSelectAllCustomers() {

        underTest.selectAllCustomers();
  
        verify(customerRepository).findAll();

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
            "hossein","hossein@email.com",19,true);
        
        underTest.updateCustomer(customer);

        verify(customerRepository).save(customer);

    }
}
