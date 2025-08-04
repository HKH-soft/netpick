package com.hossein.spring_project.customer;

import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hossein.spring_project.jwt.JWTUtil;


@RestController
@RequestMapping("v1/customers")
class CustomerController {
    
    private final CustomerService customerService;
    private final JWTUtil jwtUtil;
	public CustomerController(CustomerService customerService , JWTUtil jwtUtil) {
        this.customerService = customerService;
		this.jwtUtil = jwtUtil;
    }

    @GetMapping
	public List<CustomerDTO> getResponse(@RequestParam(defaultValue = "1") Integer page){
		return customerService.getAllCustomers(page <= 1 ? 0 : --page);
	}
	
	@GetMapping("{id}") 
	public CustomerDTO getCustomer(@PathVariable("id") Integer id){
		return customerService.getCustomerById(id);
	}

	@PostMapping
	public ResponseEntity<?> addCustomer(
			@RequestBody CustomerRegistrationRequest request){
		customerService.addCustomer(request);
		String jwtToken = jwtUtil.issueToken(request.email(),"ROLE_USER");
		return ResponseEntity.ok()
				.header(HttpHeaders.AUTHORIZATION,jwtToken)
				.build();
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
