package com.reposcope.backend.service;

import com.reposcope.backend.dto.request.UpdateProfileRequest;
import com.reposcope.backend.dto.response.ProfileResponse;
import com.reposcope.backend.model.User;
import com.reposcope.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public ProfileResponse getProfile(String email) {
        User user = userRepository.findByUserEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return new ProfileResponse(
                user.getUserId().toString(),
                user.getUserName(),
                user.getUserEmail(),
                user.getGithubUsername(),
                user.getCreatedAt().toString()
        );
    }

    public ProfileResponse updateProfile(String email, UpdateProfileRequest request) {
        User user = userRepository.findByUserEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (request.getUserName() != null) {
            user.setUserName(request.getUserName());
        }
        if (request.getUserEmail() != null) {
            user.setUserEmail(request.getUserEmail());
        }
        if (request.getUserPassword() != null) {
            user.setUserPassword(passwordEncoder.encode(request.getUserPassword()));
        }
        if (request.getGithubUsername() != null) {
            user.setGithubUsername(request.getGithubUsername());
        }

        User saved = userRepository.save(user);

        return new ProfileResponse(
                saved.getUserId().toString(),
                saved.getUserName(),
                saved.getUserEmail(),
                saved.getGithubUsername(),
                saved.getCreatedAt().toString()
        );
    }

    public void deleteProfile(String email) {
        User user = userRepository.findByUserEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        userRepository.delete(user);
    }
}