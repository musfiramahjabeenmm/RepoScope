package com.reposcope.backend.controller;

import com.reposcope.backend.dto.request.AnalyzeRepoRequest;
import com.reposcope.backend.dto.response.AnalyzeRepoResponse;
import com.reposcope.backend.dto.response.HealthCheckResponse;
import com.reposcope.backend.dto.response.HistoryResponse;
import com.reposcope.backend.dto.response.IssueRoadmapResponse;
import com.reposcope.backend.service.RepoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/repository")
@RequiredArgsConstructor
public class RepoController {

    private final RepoService repoService;

    @PostMapping("/analyze")
    public ResponseEntity<AnalyzeRepoResponse> analyze(
            @RequestBody AnalyzeRepoRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        boolean forceRefresh = request.getForceRefresh() != null && request.getForceRefresh();
        return ResponseEntity.ok(
                repoService.analyze(request.getRepositoryUrl(),
                        userDetails.getUsername(), forceRefresh));
    }


}