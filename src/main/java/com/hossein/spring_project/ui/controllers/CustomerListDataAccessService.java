package com.hossein.spring_project.ui.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

@Repository("list")
public class CustomerListDataAccessService implements CustomerDAO {
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

	@Override
	public void createCustomer(Customer customer) {
		customers.add(customer);
	}

	@Override
	public boolean existsPersonWithEmail(String email) {
		return customers.stream()
			.anyMatch(c -> c.getEmail().equals(email));
		}
		
	@Override
	public boolean existsPersonWithId(Integer id) {
		return customers.stream()
			.anyMatch(c -> c.getId().equals(id));
		
	}

	@Override
	public void removeCustomer(Integer id) {
			customers.stream()
			.filter(c -> c.getId().equals(id))
			.findFirst()
			.ifPresent(customers::remove);
	}

	@Override
	public void updateCustomer(Customer customer) {
		customers.add(customer);
	}
    
}
