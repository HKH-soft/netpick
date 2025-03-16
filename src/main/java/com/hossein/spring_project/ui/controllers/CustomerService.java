package com.hossein.spring_project.ui.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.hossein.spring_project.exception.DuplicateResource;
import com.hossein.spring_project.exception.ResourceNotFound;

@Service
public class CustomerService {
    private final CustomerDAO customerDao;

    public CustomerService(@Qualifier("jpa") CustomerDAO customerDao) {
        this.customerDao = customerDao;
    }

    public List<Customer> getAllCustomers(){
        return customerDao.selectAllCustomers();
    }

    public Customer getCustomerById(Integer id){
        return customerDao.selectCustomerById(id)
            .orElseThrow(() -> new ResourceNotFound(
                "customer with the id: [%s] was not found!".formatted(id)));
    }

    public void addCustomer(CustomerRegistrationRequest CustomerRegistrationRequest){
        if (customerDao.existsPersonWithEmail(CustomerRegistrationRequest.email())){
            throw new DuplicateResource("email already exists.");
        }
        Customer customer = new Customer(
            CustomerRegistrationRequest.name(),
            CustomerRegistrationRequest.email(),
            CustomerRegistrationRequest.age()
        );
        customerDao.createCustomer(customer);
    }
}
