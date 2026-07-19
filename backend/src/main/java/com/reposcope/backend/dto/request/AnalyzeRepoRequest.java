package com.reposcope.backend.dto.request;

import lombok.Data;

@Data
public class AnalyzeRepoRequest
{
    private String repositoryUrl;
    private Boolean forceRefresh;
}