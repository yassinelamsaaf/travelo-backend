package com.example.travelo_backend.controller;

import com.example.travelo_backend.model.Flight;
import com.example.travelo_backend.service.FlightService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

/**
 * Test class for FlightController
 * 
 * Purpose: Verify flight search functionality through the REST API
 * 
 * What it tests:
 * - One-way flight search returns correct data
 * - Round-trip flight search returns both outbound and return flights
 * - HTTP status codes are correct
 * - JSON response structure is valid
 * 
 * Testing approach:
 * - @WebMvcTest isolates only the controller layer
 * - @MockitoBean creates a mock FlightService (we don't test the real database)
 * - MockMvc simulates HTTP requests
 * - Mockito stubs the service methods to return test data
 */
@WebMvcTest(FlightController.class)
class FlightControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FlightService flightService;

    private Flight testFlight;

    @BeforeEach
    void setUp() {
        // Arrange: Create a test flight object
        testFlight = new Flight();
        testFlight.setDepartureCity("Casablanca");
        testFlight.setDepartureAirport("CMN");
        testFlight.setArrivalCity("Paris");
        testFlight.setArrivalAirport("CDG");
        testFlight.setCompany("Royal Air Maroc");
        testFlight.setPrice(new BigDecimal("250.00"));
        testFlight.setDepartureTime(LocalTime.of(10, 30));
        testFlight.setArrivalTime(LocalTime.of(15, 45));
    }

    @Test
    void testSearchFlights_OneWay_ShouldReturnFlights() throws Exception {
        // Arrange: Mock service to return test flights
        List<Flight> flights = Arrays.asList(testFlight);
        when(flightService.searchOneWayFlights(anyString(), anyString(), any(Date.class)))
                .thenReturn(flights);

        // Act & Assert: Send GET request and verify response
        mockMvc.perform(get("/api/flights/search")
                        .param("from", "Casablanca")
                        .param("to", "Paris")
                        .param("departureDate", "2025-01-15"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].departureCity").value("Casablanca"))
                .andExpect(jsonPath("$[0].arrivalCity").value("Paris"))
                .andExpect(jsonPath("$[0].company").value("Royal Air Maroc"));
    }

    @Test
    void testSearchFlights_RoundTrip_ShouldReturnBothWays() throws Exception {
        // Arrange: Mock both outbound and return flights
        Flight returnFlight = new Flight();
        returnFlight.setDepartureCity("Paris");
        returnFlight.setArrivalCity("Casablanca");
        
        when(flightService.searchOneWayFlights(anyString(), anyString(), any(Date.class)))
                .thenReturn(Arrays.asList(testFlight));
        when(flightService.searchReturnFlights(anyString(), anyString(), any(Date.class)))
                .thenReturn(Arrays.asList(returnFlight));

        // Act & Assert: Verify round-trip search returns both flight lists
        mockMvc.perform(get("/api/flights/search")
                        .param("from", "Casablanca")
                        .param("to", "Paris")
                        .param("departureDate", "2025-01-15")
                        .param("returnDate", "2025-01-22"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.oneWayFlights[0].departureCity").value("Casablanca"))
                .andExpect(jsonPath("$.returnFlights[0].departureCity").value("Paris"));
    }
}
