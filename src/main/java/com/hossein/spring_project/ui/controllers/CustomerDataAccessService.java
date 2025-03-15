package com.hossein.spring_project.ui.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

@Repository
public class CustomerDataAccessService implements CustomerDAO {
    private static List<Customer> customers;

	static{
		customers = new ArrayList<>();
		Customer hossein = new Customer(
			1,
			"hossein",
			"hossein@khk.com",
			19
		);
		customers.add(hossein);
		Customer mike = new Customer(
			2,
			"mike",
			"mike@khk.com",
			21
		);
		customers.add(mike);
	}

    @Override
    public List<Customer> selectAllCustomers() {
        return customers;
    }

    @Override
    public Optional<Customer> selectCustomerById(Integer customerId){
        return customers.stream()
		.filter(u -> u.getId().equals(customerId))
		.findFirst();
    }
    
}
