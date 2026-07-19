package com.reposcope.backend.dto.response;

import lombok.Data;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
public class ProfileResponse {
    private String userId;
    private String userName;
    private String userEmail;
    private String githubUsername;
    private String createdAt;
}