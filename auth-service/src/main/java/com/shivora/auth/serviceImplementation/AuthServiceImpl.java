
package com.shivora.auth.serviceImplementation;

import java.time.LocalDateTime;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shivora.auth.dto.RegisterRequest;
import com.shivora.auth.entity.AuthUser;
import com.shivora.auth.repository.AuthUserRepository;
import com.shivora.auth.security.JwtService;
import com.shivora.auth.service.AuthService;

@Service
public class AuthServiceImpl implements AuthService {

    private final AuthUserRepository authUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthServiceImpl(
            AuthUserRepository authUserRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService) {

        this.authUserRepository = authUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    // REGISTER NEW USER
    @Override
    @Transactional
    public String register(RegisterRequest request) {

        if (!request.getPassword().equals(
                request.getConfirmPassword())) {
            throw new IllegalArgumentException(
                    "Password and confirm password do not match");
        }

        String email = request.getEmail().trim();
        String mobile = request.getMobile().trim();

        if (authUserRepository
                .existsByEmailIgnoreCaseAndIsDeletedFalse(email)) {
            throw new IllegalArgumentException(
                    "Email is already registered");
        }

        if (authUserRepository
                .existsByMobileAndIsDeletedFalse(mobile)) {
            throw new IllegalArgumentException(
                    "Mobile number is already registered");
        }

        AuthUser user = new AuthUser();

        user.setFullName(request.getFullName().trim());
        user.setEmail(email);
        user.setMobile(mobile);
        user.setPasswordHash(
                passwordEncoder.encode(request.getPassword()));

        user.setStatus("PENDING_VERIFICATION");
        user.setFailedAttempts(0);
        user.setLockedUntil(null);

        authUserRepository.save(user);

        return "Registration successful. Please verify your account.";
    }

    // LOGIN USER
    @Override
    @Transactional
    public String login(String email, String password) {

        AuthUser user = authUserRepository
                .findActiveByEmail(email.trim())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Invalid email or password"));

        LocalDateTime now = LocalDateTime.now();

        if (user.getLockedUntil() != null) {

            if (user.getLockedUntil().isAfter(now)) {
                throw new IllegalArgumentException(
                        "Account is temporarily locked");
            }

            user.setLockedUntil(null);
            user.setFailedAttempts(0);
        }

        if (!passwordEncoder.matches(
                password, user.getPasswordHash())) {

            int attempts = user.getFailedAttempts() + 1;
            user.setFailedAttempts(attempts);

            if (attempts >= 5) {
                user.setLockedUntil(now.plusMinutes(15));
            }

            authUserRepository.save(user);

            throw new IllegalArgumentException(
                    "Invalid email or password");
        }

        if (!"ACTIVE".equalsIgnoreCase(user.getStatus())) {
            throw new IllegalArgumentException(
                    "Account is not active");
        }

        user.setFailedAttempts(0);
        user.setLockedUntil(null);
        user.setLastLoginAt(now);

        authUserRepository.save(user);

        return jwtService.generateToken(user.getEmail());
    }
}
