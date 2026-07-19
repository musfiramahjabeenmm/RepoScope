package com.reposcope.backend.dto.response;

import lombok.Data;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
public class HistoryResponse {
    private String historyId;
    private String repositoryName;
    private String repositoryUrl;
    private String analyzedAt;
}