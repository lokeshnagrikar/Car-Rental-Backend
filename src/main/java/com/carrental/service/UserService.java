package com.carrental.service;

import com.carrental.dto.request.PasswordResetRequest;
import com.carrental.dto.request.PasswordUpdateRequest;
import com.carrental.dto.request.SignupRequest;
import com.carrental.dto.response.UserResponse;
import com.carrental.exception.ResourceNotFoundException;
import com.carrental.model.Role;
import com.carrental.model.User;
import com.carrental.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final FileStorageService fileStorageService;

    @Transactional
    public UserResponse createUser(SignupRequest signupRequest) {
        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            throw new IllegalArgumentException("Email is already in use!");
        }

        User user = User.builder()
                .name(signupRequest.getName())
                .email(signupRequest.getEmail())
                .password(passwordEncoder.encode(signupRequest.getPassword()))
                .phone(signupRequest.getPhone())
                .dateOfBirth(signupRequest.getDateOfBirth())
                .address(signupRequest.getAddress())
                .drivingLicense(signupRequest.getDrivingLicense())
                .role(Role.CUSTOMER)
                .enabled(true)
                .build();

        User savedUser = userRepository.save(user);
        return mapToUserResponse(savedUser);
    }

    @Transactional
    public UserResponse createAdmin(SignupRequest signupRequest) {
        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            throw new IllegalArgumentException("Email is already in use!");
        }

        User user = User.builder()
                .name(signupRequest.getName())
                .email(signupRequest.getEmail())
                .password(passwordEncoder.encode(signupRequest.getPassword()))
                .phone(signupRequest.getPhone())
                .dateOfBirth(signupRequest.getDateOfBirth())
                .address(signupRequest.getAddress())
                .drivingLicense(signupRequest.getDrivingLicense())
                .role(Role.ADMIN)
                .enabled(true)
                .build();

        User savedUser = userRepository.save(user);
        return mapToUserResponse(savedUser);
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::mapToUserResponse)
                .collect(Collectors.toList());
    }

    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        return mapToUserResponse(user);
    }

    public UserResponse getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
        return mapToUserResponse(user);
    }

    @Transactional
    public UserResponse updateUser(Long id, SignupRequest signupRequest) {
        log.info("Updating user with ID: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        user.setName(signupRequest.getName());

        // Only check email uniqueness if it's changed
        if (!user.getEmail().equals(signupRequest.getEmail())) {
            if (userRepository.existsByEmail(signupRequest.getEmail())) {
                throw new IllegalArgumentException("Email is already in use!");
            }
            user.setEmail(signupRequest.getEmail());
        }

        // Update additional fields
        user.setPhone(signupRequest.getPhone());
        user.setDateOfBirth(signupRequest.getDateOfBirth());
        user.setAddress(signupRequest.getAddress());
        user.setDrivingLicense(signupRequest.getDrivingLicense());

        // Only update password if provided
        if (signupRequest.getPassword() != null && !signupRequest.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
        }

        User updatedUser = userRepository.save(user);
        log.info("User updated successfully: {}", updatedUser.getId());
        return mapToUserResponse(updatedUser);
    }

    @Transactional
    public UserResponse updateProfilePicture(Long userId, MultipartFile file) {
        log.info("Updating profile picture for user ID: {}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        try {
            // Delete old profile picture if exists
            if (user.getProfilePicture() != null && !user.getProfilePicture().isEmpty()) {
                log.info("Deleting old profile picture: {}", user.getProfilePicture());
                fileStorageService.deleteFile(user.getProfilePicture());
            }

            // Store the new profile picture
            String fileName = fileStorageService.storeFile(file);
            user.setProfilePicture(fileName);

            User updatedUser = userRepository.save(user);
            log.info("Profile picture updated for user ID: {}", userId);
            return mapToUserResponse(updatedUser);
        } catch (Exception e) {
            log.error("Error updating profile picture for user ID: {}", userId, e);
            throw new RuntimeException("Could not update profile picture: " + e.getMessage(), e);
        }
    }

    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }

    @Transactional
    public void initiatePasswordReset(PasswordResetRequest request) {
        try {
            User user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + request.getEmail()));

            String token = UUID.randomUUID().toString();
            user.setResetToken(token);
            userRepository.save(user);

            // Send email (this won't throw exceptions now)
            emailService.sendPasswordResetEmail(user.getEmail(), token);

            log.info("Password reset initiated for user: {}", request.getEmail());
        } catch (Exception e) {
            log.error("Error initiating password reset for email: {}", request.getEmail(), e);
            throw e;
        }
    }

    @Transactional
    public void resetPassword(PasswordUpdateRequest request) {
        try {
            User user = userRepository.findByResetToken(request.getToken())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid or expired token"));

            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setResetToken(null);
            userRepository.save(user);

            log.info("Password reset successful for user ID: {}", user.getId());
        } catch (Exception e) {
            log.error("Error resetting password with token: {}", request.getToken(), e);
            throw e;
        }
    }

    private UserResponse mapToUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .dateOfBirth(user.getDateOfBirth())
                .address(user.getAddress())
                .drivingLicense(user.getDrivingLicense())
                .profilePicture(user.getProfilePicture())
                .role(user.getRole())
                .build();
    }
}
