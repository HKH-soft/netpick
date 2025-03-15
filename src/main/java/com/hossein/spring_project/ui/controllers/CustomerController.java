package com.hossein.spring_project.ui.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/customers")
class CustomerController {
    
    private final CustomerService customerService;
    
	public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
	public List<Customer> getResponse(){
		return customerService.getAllCustomers();
	}
	
	@GetMapping("{id}")
	public Customer getCustomer(@PathVariable("id") Integer id){
		return customerService.getCustomerById(id);
	}

}
