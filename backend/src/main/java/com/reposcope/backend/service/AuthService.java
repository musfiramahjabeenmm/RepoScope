package com.reposcope.backend.service;

import com.reposcope.backend.config.JwtUtil;
import com.reposcope.backend.dto.request.LoginRequest;
import com.reposcope.backend.dto.request.SignupRequest;
import com.reposcope.backend.dto.response.LoginResponse;
import com.reposcope.backend.dto.response.SignupResponse;
import com.reposcope.backend.model.User;
import com.reposcope.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public SignupResponse signup(SignupRequest request) {
        if (userRepository.existsByUserEmail(request.getUserEmail())) {
            throw new RuntimeException("Email already registered");
        }

        User user = new User();
        user.setUserName(request.getUserName());
        user.setUserEmail(request.getUserEmail());
        user.setUserPassword(passwordEncoder.encode(request.getUserPassword()));
        user.setGithubUsername(request.getGithubUsername());

        User saved = userRepository.save(user);

        return new SignupResponse(
                saved.getUserId().toString(),
                saved.getUserName(),
                "Account created successfully"
        );
    }

    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByUserEmail(request.getUserEmail())
                .orElseThrow(() -> new RuntimeException("Account not found"));

        if (!passwordEncoder.matches(request.getUserPassword(), user.getUserPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        String token = jwtUtil.generateToken(user.getUserEmail());

        return new LoginResponse(
                token,
                user.getUserName(),
                user.getUserEmail()
        );
    }
}