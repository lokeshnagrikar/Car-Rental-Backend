package com.carrental;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CarRentalApplication {
	// The @SpringBootApplication annotation is a convenience annotation
	// that adds @Configuration, @EnableAutoConfiguration, and @ComponentScan.
	// It indicates that this class is the main configuration class for the Spring Boot application.

	// The main method is the entry point of the Java application.
	// It uses SpringApplication.run to launch the application.
	// The SpringApplication.run method bootstraps the application,
	// starting the embedded server (like Tomcat) and initializing the Spring context.
	// The CarRentalApplication class is the main entry point of the application.

	// Main method: the entry point of the Java application
	// SpringApplication.run: bootstraps the application and starts the embedded server
	public static void main(String[] args) {
		// Starting point of the Spring Boot application
		// The SpringApplication.run method bootstraps the application
		// and starts the embedded server.
		// The CarRentalApplication class is the main entry point of the application.
		SpringApplication.run(CarRentalApplication.class, args);
	}


}
// This class is annotated with @SpringBootApplication, which is a convenience
// annotation that adds @Configuration, @EnableAutoConfiguration, and @ComponentScan.
// It indicates that this class is the main configuration class for the Spring Boot application.
// The main method uses SpringApplication.run to launch the application.