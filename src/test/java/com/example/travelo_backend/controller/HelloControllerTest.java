package com.example.travelo_backend.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * Test class for HelloController
 * 
 * Purpose: Verify that the /api/hello endpoint works correctly
 * 
 * What it tests:
 * - The endpoint returns HTTP 200 (OK)
 * - The response body contains the expected message
 * 
 * Testing approach:
 * - Uses @WebMvcTest to test only the web layer (controller)
 * - MockMvc simulates HTTP requests without starting the full server
 */
@WebMvcTest(HelloController.class)
class HelloControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testHelloEndpoint_ShouldReturnHelloWorld() throws Exception {
        // Act & Assert: Send GET request to /api/hello and verify response
        mockMvc.perform(get("/api/hello"))
                .andExpect(status().isOk())
                .andExpect(content().string("Hello, World!"));
    }
}
