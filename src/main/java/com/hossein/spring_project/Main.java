package com.hossein.spring_project;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.hossein.spring_project.ui.controllers.Customer;
import com.hossein.spring_project.ui.controllers.CustomerRepository;

@SpringBootApplication
public class Main{
	
	public static void main(String[] args){
		SpringApplication.run(Main.class,args);
	}
	@Bean
	CommandLineRunner runner(CustomerRepository customerRepository){


		return args -> {
			Customer hossein = new Customer(
				"hossein",
				"hossein@khk.com",
				19
			);
			Customer mike = new Customer(
				"mike",
				"mike@khk.com",
				21
			);
			List<Customer> customers = List.of(hossein,mike);
			// customerRepository.saveAll(customers);
			
		};
	}	
}