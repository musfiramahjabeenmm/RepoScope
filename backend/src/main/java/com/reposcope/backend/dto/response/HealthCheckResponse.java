package com.reposcope.backend.dto.response;

import lombok.Data;
import lombok.AllArgsConstructor;
import java.util.Map;

@Data
@AllArgsConstructor
public class HealthCheckResponse {
    private String repositoryName;
    private String owner;
    private Integer healthScore;
    private Map<String, Double> breakdown;
    private String analyzedAt;
}