package com.carrental;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CarRentalApplication {
	// The CarRentalApplication class is the main entry point of the Spring Boot application.
	// It uses the @SpringBootApplication annotation to enable autoconfiguration and component scanning.
	// The main method starts the Spring Boot application by calling SpringApplication.run().



	public static void main(String[] args) {
		SpringApplication.run(CarRentalApplication.class, args);
		// The main method is the starting point of the application.
		// It uses SpringApplication.run() to launch the application.
		// Any command-line arguments can be passed to the application through the args parameter.
		// The application will start with the default configurations and scan for components in the specified package.
		// The application will also load the embedded web server (like Tomcat) and start listening for incoming requests.
		// This method is responsible for bootstrapping the entire Spring application context.

		// The application will start running and be ready to handle incoming requests.
		// Any configuration properties defined in the application.properties or application.yml files will be loaded.
		// The application will also perform any necessary database migrations if configured.
		// Once the application is started, it will log the startup information to the console or configured logging destination.

		// The main method is typically the entry point of a Java application,
		// and in this case, it serves as the starting point for the Spring Boot application.

		// Additional configurations or initializations can be done here if needed.
	}

}
