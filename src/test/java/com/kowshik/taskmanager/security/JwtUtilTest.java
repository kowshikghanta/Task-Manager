package com.kowshik.taskmanager.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
    }

    @Test
    void testGenerateAndValidateToken() {
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                "test@example.com", 
                "password123", 
                Collections.emptyList()
        );

        String token = jwtUtil.generateToken(userDetails);
        assertNotNull(token);

        String extractedUsername = jwtUtil.extractUsername(token);
        assertEquals("test@example.com", extractedUsername);

        assertTrue(jwtUtil.validateToken(token, userDetails));
    }
}
