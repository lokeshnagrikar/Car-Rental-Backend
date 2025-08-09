package com.carrental.controller;

import com.carrental.dto.request.LoginRequest;
import com.carrental.dto.request.PasswordResetRequest;
import com.carrental.dto.request.PasswordUpdateRequest;
import com.carrental.dto.request.SignupRequest;
import com.carrental.dto.response.JwtResponse;
import com.carrental.dto.response.MessageResponse;
import com.carrental.dto.response.UserResponse;
import com.carrental.model.User;
import com.carrental.security.jwt.JwtUtils;
import com.carrental.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Authentication API")
public class AuthController {

    // The AuthController class handles authentication-related requests,
    // such as login, signup, password reset, and token refresh.
    // It uses the AuthenticationManager to authenticate users,
    // the UserService to manage user data, and the JwtUtils to generate and validate JWT tokens.

    // The AuthenticationManager is used to authenticate users based on their credentials.
    // The UserService is responsible for user-related operations, such as creating users and managing roles.
    // The JwtUtils is used for generating and validating JWT tokens for secure communication.

    // The @RequestMapping annotation is used to specify the base URL for all endpoints in this controller.
    // The @RestController annotation indicates that this class is a RESTful controller,
    // and the @RequiredArgsConstructor annotation generates a constructor with required arguments for dependency injection.

    // The @Operation annotation is used to document the API endpoints for OpenAPI (Swagger) documentation.
    // The @Tag annotation is used to group related API endpoints under a common tag in the documentation.

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtUtils jwtUtils;

    @PostMapping("/login")
    @Operation(summary = "Authenticate user and generate JWT token")
    public ResponseEntity<JwtResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        String refreshToken = jwtUtils.generateRefreshToken(loginRequest.getEmail());

        User userDetails = (User) authentication.getPrincipal();

        return ResponseEntity.ok(JwtResponse.builder()
                .token(jwt)
                .refreshToken(refreshToken)
                .id(userDetails.getId())
                .name(userDetails.getName())
                .email(userDetails.getUsername())
                .role(userDetails.getRole().name())
                .build());
    }

    @PostMapping("/signup")
    @Operation(summary = "Register a new user")
    public ResponseEntity<UserResponse> registerUser(@Valid @RequestBody SignupRequest signupRequest) {
        log.info("Signup request: {}", signupRequest); // <— log input
        UserResponse user = userService.createUser(signupRequest);
        log.info("User created: {}", user); // <— log output
        return ResponseEntity.ok(user);
    }

    @PostMapping("/refresh-token")
    @Operation(summary = "Refresh JWT token")
    public ResponseEntity<JwtResponse> refreshToken(@RequestParam String refreshToken) {
        if (!jwtUtils.validateJwtToken(refreshToken)) {
            return ResponseEntity.badRequest().body(null);
        }

        String username = jwtUtils.getUserNameFromJwtToken(refreshToken);
        String newToken = jwtUtils.generateTokenFromUsername(username);
        String newRefreshToken = jwtUtils.generateRefreshToken(username);

        return ResponseEntity.ok(JwtResponse.builder()
                .token(newToken)
                .refreshToken(newRefreshToken)
                .type("Bearer")
                .build());
    }

    @PostMapping("/password-reset-request")
    @Operation(summary = "Request password reset")
    public ResponseEntity<MessageResponse> requestPasswordReset(@Valid @RequestBody PasswordResetRequest request) {
        userService.initiatePasswordReset(request);
        return ResponseEntity.ok(new MessageResponse("Password reset email sent"));
    }

    @PostMapping("/password-reset")
    @Operation(summary = "Reset password with token")
    public ResponseEntity<MessageResponse> resetPassword(@Valid @RequestBody PasswordUpdateRequest request) {
        userService.resetPassword(request);
        return ResponseEntity.ok(new MessageResponse("Password has been reset successfully"));
    }

    // Add this method to create an initial admin user for testing
    @PostMapping("/init")
    @Operation(summary = "Initialize with admin user")
    public ResponseEntity<MessageResponse> initializeAdmin() {
        // Check if admin already exists
        try {
            SignupRequest adminRequest = new SignupRequest();
            adminRequest.setName("Admin User");
            adminRequest.setEmail("admin@example.com");
            adminRequest.setPassword("admin123");
            userService.createAdmin(adminRequest);
            return ResponseEntity.ok(new MessageResponse("Admin user created successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.ok(new MessageResponse("Admin user already exists"));
        }
    }
}
