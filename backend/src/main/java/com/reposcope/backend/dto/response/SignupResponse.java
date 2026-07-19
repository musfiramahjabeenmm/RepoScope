package com.reposcope.backend.dto.response;

import lombok.Data;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
public class SignupResponse {
    private String userId;
    private String userName;
    private String message;
}