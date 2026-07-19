package com.reposcope.backend.dto.response;

import lombok.Data;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
public class IssueRoadmapResponse {
    private String repositoryName;
    private String issueTree;
    private String analyzedAt;
}