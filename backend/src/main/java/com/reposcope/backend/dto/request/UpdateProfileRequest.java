package com.reposcope.backend.dto.request;

import lombok.Data;

@Data
public class UpdateProfileRequest {
    private String userName;
    private String userEmail;
    private String userPassword;
    private String githubUsername;
}