package com.example.travelo_backend.service;

import com.example.travelo_backend.model.Flight;
import com.example.travelo_backend.repository.FlightRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Test class for FlightService
 * 
 * Purpose: Verify business logic for flight search operations
 * 
 * What it tests:
 * - searchOneWayFlights correctly calls the repository and returns results
 * - searchReturnFlights correctly calls the repository and returns results
 * - Service methods transform data correctly (if any transformation exists)
 * - Proper delegation to the repository layer
 * 
 * Testing approach:
 * - @ExtendWith(MockitoExtension.class) enables Mockito annotations
 * - @Mock creates a mock FlightRepository
 * - @InjectMocks creates FlightService and injects the mock repository
 * - Pure unit test: no Spring context, just testing the service logic
 */
@ExtendWith(MockitoExtension.class)
class FlightServiceTest {

    @Mock
    private FlightRepository flightRepository;

    @InjectMocks
    private FlightService flightService;

    private Flight testFlight;
    private Date testDate;

    @BeforeEach
    void setUp() {
        // Arrange: Create test data
        testFlight = new Flight();
        testFlight.setDepartureCity("Casablanca");
        testFlight.setDepartureAirport("CMN");
        testFlight.setArrivalCity("Paris");
        testFlight.setArrivalAirport("CDG");
        testFlight.setCompany("Royal Air Maroc");
        testFlight.setPrice(new BigDecimal("250.00"));
        testFlight.setDepartureTime(LocalTime.of(10, 30));
        testFlight.setArrivalTime(LocalTime.of(15, 45));
        
        testDate = new Date();
    }

    @Test
    void testSearchOneWayFlights_ShouldReturnFlightsFromRepository() {
        // Arrange: Mock repository to return test flights
        List<Flight> expectedFlights = Arrays.asList(testFlight);
        when(flightRepository.findOneWayFlights(anyString(), anyString(), any(Date.class)))
                .thenReturn(expectedFlights);

        // Act: Call service method
        List<Flight> result = flightService.searchOneWayFlights("Casablanca", "Paris", testDate);

        // Assert: Verify results and that repository was called
        assertNotNull(result, "Result should not be null");
        assertEquals(1, result.size(), "Should return 1 flight");
        assertEquals("Casablanca", result.get(0).getDepartureCity());
        assertEquals("Paris", result.get(0).getArrivalCity());
        
        // Verify repository method was called exactly once with correct parameters
        verify(flightRepository).findOneWayFlights("Casablanca", "Paris", testDate);
    }

    @Test
    void testSearchReturnFlights_ShouldReturnFlightsFromRepository() {
        // Arrange: Create return flight (reversed route)
        Flight returnFlight = new Flight();
        returnFlight.setDepartureCity("Paris");
        returnFlight.setArrivalCity("Casablanca");
        
        when(flightRepository.findReturnFlights(anyString(), anyString(), any(Date.class)))
                .thenReturn(Arrays.asList(returnFlight));

        // Act: Call service method
        List<Flight> result = flightService.searchReturnFlights("Casablanca", "Paris", testDate);

        // Assert: Verify return flights are found correctly
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Paris", result.get(0).getDepartureCity());
        assertEquals("Casablanca", result.get(0).getArrivalCity());
        
        verify(flightRepository).findReturnFlights("Casablanca", "Paris", testDate);
    }

    @Test
    void testSearchOneWayFlights_NoFlightsFound_ShouldReturnEmptyList() {
        // Arrange: Mock repository to return empty list
        when(flightRepository.findOneWayFlights(anyString(), anyString(), any(Date.class)))
                .thenReturn(Arrays.asList());

        // Act: Search for flights
        List<Flight> result = flightService.searchOneWayFlights("Unknown", "Unknown", testDate);

        // Assert: Empty list is returned (not null)
        assertNotNull(result);
        assertTrue(result.isEmpty(), "Should return empty list when no flights found");
    }
}
