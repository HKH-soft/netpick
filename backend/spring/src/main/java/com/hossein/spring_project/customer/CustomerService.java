package com.hossein.spring_project.customer;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.hossein.spring_project.exception.DuplicateResourceExeption;
import com.hossein.spring_project.exception.RequestValidationExeption;
import com.hossein.spring_project.exception.ResourceNotFoundExeption;

@Service
public class CustomerService {
    private final CustomerDAO customerDao;
    private final PasswordEncoder passwordEncoder;
    private final CustomerDTOMapper customerDTOMapper;
    public CustomerService(@Qualifier("jdbc") CustomerDAO customerDao, PasswordEncoder passwordEncoder,CustomerDTOMapper customerDTOMapper) {
        this.customerDao = customerDao;
        this.passwordEncoder = passwordEncoder;
        this.customerDTOMapper = customerDTOMapper;
    }

    public List<CustomerDTO> getAllCustomers(){
        return customerDao.selectAllCustomers()
                    .stream()
                    .map(customerDTOMapper)
                    .collect(Collectors.toList());
                
    }

    public CustomerDTO getCustomerById(Integer id){
        return customerDao.selectCustomerById(id)
                    .map(customerDTOMapper)
                    .orElseThrow(() -> new ResourceNotFoundExeption(
                        "customer with the id: [%s] was not found!".formatted(id)));
    }

    public void addCustomer(CustomerRegistrationRequest CustomerRegistrationRequest){
        String email = CustomerRegistrationRequest.email();
        if (customerDao.existsPersonWithEmail(email)){
            throw new DuplicateResourceExeption("email already exists.");
        }
        if( CustomerRegistrationRequest.name()      == null || 
            CustomerRegistrationRequest.email()     == null ||
            CustomerRegistrationRequest.password()  == null ||
            CustomerRegistrationRequest.age()       == null ||
            CustomerRegistrationRequest.gender()    == null ){
            throw new RequestValidationExeption("There is an empty parameter.");
        }
        Customer customer = new Customer(
            CustomerRegistrationRequest.name(),
            CustomerRegistrationRequest.email(),
            passwordEncoder.encode(CustomerRegistrationRequest.password()),
            CustomerRegistrationRequest.age(), CustomerRegistrationRequest.gender()
        );
        customerDao.createCustomer(customer);
    }

    public void deleteCustomer(Integer id){
        if(!customerDao.existsPersonWithId(id)){
            throw new ResourceNotFoundExeption("customer with the id: [%s] was not found!".formatted(id));
        }
        customerDao.removeCustomer(id);
    }

    public void updateCustomer(CustomerUpdateRequest customerRequest , Integer id){
        if(!customerDao.existsPersonWithId(id)){
            throw new ResourceNotFoundExeption("customer with the id: [%s] was not found!".formatted(id));
        }
        boolean changed = false;

        Customer customer = customerDao.selectCustomerById(id)
                    .orElseThrow(() -> new ResourceNotFoundExeption(
                        "customer with the id: [%s] was not found!".formatted(id)));
                        
        if(customerRequest.name() != null && !customer.getName().equals(customerRequest.name())){
            customer.setName(customerRequest.name());
            changed = true;
        }
        if(customerRequest.age() != null && !customer.getAge().equals(customerRequest.age())){
            customer.setAge(customerRequest.age());
            changed = true;
        }
        if(customerRequest.email() != null && !customer.getEmail().equals(customerRequest.email())){
            String email = customerRequest.email();
            if (customerDao.existsPersonWithEmail(email)){
                throw new DuplicateResourceExeption("email already exists.");
            }
            customer.setEmail(customerRequest.email());
            changed = true;
        }
        if(customerRequest.gender() != null && !customer.getGender().equals(customerRequest.gender())){
            customer.setGender(customerRequest.gender());
            changed = true;
        }
        if (!changed) {
            throw new RequestValidationExeption("there were no changes.");
        }
        customerDao.updateCustomer(customer);
    }
}
