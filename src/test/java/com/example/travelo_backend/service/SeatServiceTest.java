package com.example.travelo_backend.service;

import dto.ReservationDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for SeatService
 * 
 * Purpose: Verify seat reservation business logic
 * 
 * What it tests:
 * - reserveSeat method returns expected success message
 * 
 * Testing approach:
 * - Pure unit test (no Spring, no mocks needed for this simple service)
 * - Tests the service method directly
 * 
 * Note: Current implementation is very simple (always returns success).
 * In a real application, this would contain more complex logic.
 */
class SeatServiceTest {

    private SeatService seatService;

    @BeforeEach
    void setUp() {
        // Arrange: Create instance of service under test
        seatService = new SeatService();
    }

    @Test
    void testReserveSeat_ShouldReturnSuccessMessage() {
        // Arrange: Create a test reservation DTO
        ReservationDto reservationDto = new ReservationDto();
        reservationDto.setFlightId(1L);
        reservationDto.setSeatName("a1");

        // Act: Call service method
        String result = seatService.reserveSeat(reservationDto);

        // Assert: Verify success message is returned
        assertNotNull(result, "Result should not be null");
        assertEquals("Seat reserved successfully", result);
    }
}
