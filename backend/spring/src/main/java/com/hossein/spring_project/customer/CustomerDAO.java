package com.hossein.spring_project.customer;

import java.util.List;
import java.util.Optional;

public interface CustomerDAO {
    List<Customer> selectAllCustomers(Integer page,Integer pageSize);
    Optional<Customer> selectCustomerById(Integer customerId);
    void createCustomer(Customer customer);
    boolean existsPersonWithEmail(String email);
    boolean existsPersonWithId(Integer id);
    void removeCustomer(Integer id);
    void updateCustomer(Customer customer);
    Optional<Customer> selectCustomerByEmail(String email);
}