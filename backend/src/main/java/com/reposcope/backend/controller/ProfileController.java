package com.reposcope.backend.controller;

import com.reposcope.backend.dto.request.UpdateProfileRequest;
import com.reposcope.backend.dto.response.ProfileResponse;
import com.reposcope.backend.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping
    public ResponseEntity<ProfileResponse> getProfile(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(
                profileService.getProfile(userDetails.getUsername()));
    }

    @PutMapping
    public ResponseEntity<ProfileResponse> updateProfile(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody UpdateProfileRequest request) {
        return ResponseEntity.ok(
                profileService.updateProfile(userDetails.getUsername(), request));
    }

    @DeleteMapping
    public ResponseEntity<String> deleteProfile(
            @AuthenticationPrincipal UserDetails userDetails) {
        profileService.deleteProfile(userDetails.getUsername());
        return ResponseEntity.ok("Account deleted successfully");
    }
}