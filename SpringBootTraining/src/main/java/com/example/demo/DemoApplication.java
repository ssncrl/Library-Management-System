package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// ==============================================================
// [STEP-1] Spring Boot Entry Point
// --------------------------------------------------------------
// @SpringBootApplication
//   A convenience annotation that combines three annotations:
//
//   @SpringBootConfiguration
//     Marks this class as a source of bean definitions.
//
//   @EnableAutoConfiguration
//     Tells Spring Boot to automatically configure the application
//     based on the dependencies found in the classpath.
//     e.g., if spring-boot-starter-web is present,
//     an embedded Tomcat server is set up automatically.
//
//   @ComponentScan
//     Scans the current package and all sub-packages for
//     Spring-managed components and registers them as beans:
//     @Component, @Service, @Repository, @Controller, etc.
//
// SpringApplication.run()
//   Bootstraps the application, starts the embedded server,
//   and initializes the Spring application context.
// ==============================================================
@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

}
