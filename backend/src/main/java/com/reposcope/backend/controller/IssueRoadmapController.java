package com.reposcope.backend.controller;

import com.reposcope.backend.dto.response.IssueRoadmapResponse;
import com.reposcope.backend.service.RepoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/repository")
@RequiredArgsConstructor
public class IssueRoadmapController {

    private final RepoService repoService;

    @GetMapping("/{repositoryId}/issues")
    public ResponseEntity<IssueRoadmapResponse> getIssueRoadmap(
            @PathVariable String repositoryId) {
        return ResponseEntity.ok(repoService.getIssueRoadmap(repositoryId));
    }
}