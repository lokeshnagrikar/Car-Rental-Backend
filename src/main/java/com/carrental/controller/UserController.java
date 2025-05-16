package com.carrental.controller;

import com.carrental.dto.request.SignupRequest;
import com.carrental.dto.response.MessageResponse;
import com.carrental.dto.response.UserResponse;
import com.carrental.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "User management API")
@SecurityRequirement(name = "bearerAuth")
@Slf4j
public class UserController {

    private final UserService userService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all users (Admin only)")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<UserResponse> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @userSecurity.isUser(authentication, #id)")
    @Operation(summary = "Get user by ID (Admin or self)")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        UserResponse user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/me")
    @Operation(summary = "Get current user profile")
    public ResponseEntity<UserResponse> getCurrentUser(Authentication authentication) {
        String email = authentication.getName();
        UserResponse user = userService.getUserByEmail(email);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create admin user (Admin only)")
    public ResponseEntity<UserResponse> createAdmin(@Valid @RequestBody SignupRequest signupRequest) {
        UserResponse user = userService.createAdmin(signupRequest);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @userSecurity.isUser(authentication, #id)")
    @Operation(summary = "Update user (Admin or self)")
    public ResponseEntity<UserResponse> updateUser(@PathVariable Long id, @Valid @RequestBody SignupRequest signupRequest) {
        log.info("Updating user with ID: {}", id);
        UserResponse updatedUser = userService.updateUser(id, signupRequest);
        return ResponseEntity.ok(updatedUser);
    }

    @PostMapping(value = "/{id}/profile-picture", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN') or @userSecurity.isUser(authentication, #id)")
    @Operation(summary = "Upload profile picture (Admin or self)")
    public ResponseEntity<?> uploadProfilePicture(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file) {

        log.info("Uploading profile picture for user ID: {}", id);

        if (file.isEmpty()) {
            log.warn("Empty file uploaded for user ID: {}", id);
            return ResponseEntity.badRequest().body(new MessageResponse("Please select a file to upload"));
        }

        // Check file type
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            log.warn("Invalid file type uploaded for user ID: {}: {}", id, contentType);
            return ResponseEntity.badRequest().body(new MessageResponse("Please upload an image file"));
        }

        try {
            UserResponse updatedUser = userService.updateProfilePicture(id, file);
            log.info("Profile picture updated successfully for user ID: {}", id);
            return ResponseEntity.ok(updatedUser);
        } catch (Exception e) {
            log.error("Error uploading profile picture for user ID: {}", id, e);
            return ResponseEntity.badRequest().body(new MessageResponse("Failed to upload profile picture: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete user (Admin only)")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
