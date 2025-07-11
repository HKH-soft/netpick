package com.hossein.spring_project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


// import org.springframework.context.annotation.Bean;
// import org.springframework.security.crypto.password.PasswordEncoder;

// import java.util.Random;
// import java.util.UUID;

// import org.springframework.beans.factory.annotation.Qualifier;
// import org.springframework.boot.CommandLineRunner;
// import com.github.javafaker.Faker;
// import com.hossein.spring_project.customer.Customer;
// import com.hossein.spring_project.customer.CustomerDAO;

@SpringBootApplication
public class Main{
	
	public static void main(String[] args){
		SpringApplication.run(Main.class,args);
	}
	// @Bean
	// CommandLineRunner runner(@Qualifier("jdbc") CustomerDAO customerDao,PasswordEncoder passwordEncoder){
		
	// 	return args -> {         
	// 		Faker faker = new Faker(); 
	// 		Random random = new Random();
	// 		for (int i = 0; i < 50; i++) {
	// 			customerDao.createCustomer(new Customer(faker.name().fullName(),faker.name().fullName().toString().replaceAll("\\s", ".").toLowerCase() + "@example.com",passwordEncoder.encode(UUID.randomUUID().toString()) ,random.nextInt(16,99),random.nextBoolean())); 
	// 		}
	// 	};
	// }
}