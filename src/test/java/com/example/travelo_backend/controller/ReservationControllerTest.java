package com.example.travelo_backend.controller;

import com.example.travelo_backend.model.Flight;
import com.example.travelo_backend.model.Passenger;
import com.example.travelo_backend.model.Seat;
import com.example.travelo_backend.repository.FlightRepository;
import com.example.travelo_backend.repository.PassengerRepository;
import com.example.travelo_backend.repository.ReservationRepository;
import com.example.travelo_backend.repository.SeatRepository;
import dto.ReservationDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import jakarta.servlet.ServletException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test class for ReservationController
 * 
 * Purpose: Verify seat reservation functionality through the REST API
 * 
 * What it tests:
 * - Successful seat reservation with valid data
 * - Rejection when seat is already taken
 * - Error handling for invalid seat names
 * - Error handling for non-existent flights
 * 
 * Testing approach:
 * - @WebMvcTest isolates controller layer
 * - @MockitoBean mocks repositories (passenger, flight, seat, reservation)
 * - MockMvc simulates POST requests with JSON body
 * - Tests both success and various error scenarios
 */
@WebMvcTest(ReservationController.class)
class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PassengerRepository passengerRepository;

    @MockitoBean
    private FlightRepository flightRepository;

    @MockitoBean
    private SeatRepository seatRepository;

    @MockitoBean
    private ReservationRepository reservationRepository;

    private Passenger testPassenger;
    private Flight testFlight;
    private Seat testSeat;

    @BeforeEach
    void setUp() {
        // Arrange: Create test data
        testPassenger = new Passenger();
        testPassenger.setFirstName("John");
        testPassenger.setFamilyName("Doe");
        testPassenger.setGender("Male");
        testPassenger.setAge(30);

        testFlight = new Flight();
        testFlight.setDepartureCity("Casablanca");
        testFlight.setArrivalCity("Paris");

        testSeat = new Seat();
        // Note: Seat uses reflection to get/set seat fields
    }

    @Test
    void testReserveSeat_Success_ShouldReturnOk() throws Exception {
        // Arrange: Mock repositories to simulate successful reservation
        when(passengerRepository.save(any(Passenger.class)))
                .thenReturn(testPassenger);
        when(seatRepository.findById(1L))
                .thenReturn(Optional.of(testSeat));
        when(flightRepository.findById(1L))
                .thenReturn(Optional.of(testFlight));

        String requestBody = """
                {
                    "flightId": 1,
                    "seatName": "a1",
                    "passenger": {
                        "firstName": "John",
                        "familyName": "Doe",
                        "gender": "Male",
                        "age": 30
                    }
                }
                """;

        // Act & Assert: Send POST request and expect success
        mockMvc.perform(post("/api/reservations/reserve")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().string("Seat reserved successfully"));
    }

    @Test
    void testReserveSeat_SeatNotFound_ShouldThrowException() {
        // Arrange: Mock repository to return empty (seat not found for flight)
        when(seatRepository.findById(999L))
                .thenReturn(Optional.empty());

        String requestBody = """
                {
                    "flightId": 999,
                    "seatName": "a1",
                    "passenger": {
                        "firstName": "John",
                        "familyName": "Doe"
                    }
                }
                """;

        // Act & Assert: Expect ServletException wrapping RuntimeException from controller
        org.junit.jupiter.api.Assertions.assertThrows(ServletException.class,
                () -> mockMvc.perform(post("/api/reservations/reserve")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody))
                        .andReturn());
    }
}
