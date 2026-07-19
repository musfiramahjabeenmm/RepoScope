package com.reposcope.backend.controller;

import com.reposcope.backend.dto.response.HealthCheckResponse;
import com.reposcope.backend.service.RepoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/repository")
@RequiredArgsConstructor
public class HealthCheckController {

    private final RepoService repoService;

    @GetMapping("/{repositoryId}/health")
    public ResponseEntity<HealthCheckResponse> getHealth(
            @PathVariable String repositoryId) {
        return ResponseEntity.ok(repoService.getHealth(repositoryId));
    }

    @GetMapping("/{repositoryId}/health/filter")
    public ResponseEntity<HealthCheckResponse> getHealthFiltered(
            @PathVariable String repositoryId,
            @RequestParam(required = false) String filter) {
        return ResponseEntity.ok(repoService.getHealth(repositoryId));
    }
}