package com.example.travelo_backend;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import com.example.travelo_backend.controller.FlightController;
import com.example.travelo_backend.controller.HelloController;
import com.example.travelo_backend.service.FlightService;

/**
 * Integration test for the Travelo Backend Application
 * 
 * Purpose: Verify that the Spring Boot application context loads successfully
 * 
 * What it tests:
 * - Application context initializes without errors
 * - All required beans are created and wired correctly
 * - Controllers and services are present in the context
 * 
 * Testing approach:
 * - @SpringBootTest loads the full application context (integration test)
 * - This is slower than unit tests but ensures the app can start
 * - Critical for CI/CD: if this fails, the app won't run in production
 */
@SpringBootTest
class TraveloBackendApplicationTests {

	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	private HelloController helloController;

	@Autowired
	private FlightController flightController;

	@Autowired
	private FlightService flightService;

	@Test
	void contextLoads() {
		// Assert: Verify Spring context loaded successfully
		assertNotNull(applicationContext, "Application context should not be null");
	}

	@Test
	void testControllersAreLoaded() {
		// Assert: Verify critical controllers are instantiated
		assertNotNull(helloController, "HelloController should be loaded");
		assertNotNull(flightController, "FlightController should be loaded");
	}

	@Test
	void testServicesAreLoaded() {
		// Assert: Verify services are instantiated
		assertNotNull(flightService, "FlightService should be loaded");
	}
}
