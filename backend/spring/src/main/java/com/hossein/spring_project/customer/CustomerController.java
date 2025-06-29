package com.hossein.spring_project.customer;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("v1/customers")
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

	@PostMapping
	public void addCustomer(
			@RequestBody CustomerRegistrationRequest request){
		customerService.addCustomer(request);
	}


	@DeleteMapping("{id}")
	public void deleteCustomer(@PathVariable("id") Integer id){
		customerService.deleteCustomer(id);
	}

	@PutMapping("{id}")
	public void updateCustomer(
		@PathVariable("id") Integer id,
		@RequestBody CustomerUpdateRequest customer
	){
		customerService.updateCustomer(customer,id);
	}

}
