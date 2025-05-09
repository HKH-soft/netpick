package com.hossein.spring_project;

import java.util.Random;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.github.javafaker.Faker;
import com.hossein.spring_project.customer.Customer;
import com.hossein.spring_project.customer.CustomerRepository;

@SpringBootApplication
public class Main{
	
	public static void main(String[] args){
		SpringApplication.run(Main.class,args);
	}
	@Bean
	CommandLineRunner runner(CustomerRepository customerRepository){
		
		return args -> {
			Faker faker = new Faker(); 
			Random random = new Random();
			var name = faker.name().fullName().toString();
			Customer customer = new Customer(
				name,
				name.replaceAll("\\s", ".")
				.toLowerCase() + "@example.com",
				random.nextInt(16,99)
			);
			customerRepository.save(customer);
			
		};
	}	
}