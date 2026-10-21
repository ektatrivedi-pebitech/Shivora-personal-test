
package com.shivora.auth.controller;

import com.shivora.auth.dto.RegisterRequest;
import com.shivora.auth.service.AuthService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(
            @Valid @RequestBody RegisterRequest request) {

        String result = authService.register(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(result);
    }
}
