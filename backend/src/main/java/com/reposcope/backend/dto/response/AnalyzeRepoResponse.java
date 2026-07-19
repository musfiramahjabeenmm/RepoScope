package com.reposcope.backend.dto.response;

import lombok.Data;
import lombok.AllArgsConstructor;
import java.util.Map;

@Data
@AllArgsConstructor
public class AnalyzeRepoResponse {
    private String repositoryName;
    private String owner;
    private Integer healthScore;
    private Map<String, Double> breakdown;
    private String issueTree;
    private String status;
    private Boolean isSoloProject;
}