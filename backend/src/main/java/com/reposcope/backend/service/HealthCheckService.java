package com.reposcope.backend.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.reposcope.backend.engine.HealthScoreEngine;
import com.reposcope.backend.model.HealthCheckResult;
import com.reposcope.backend.model.RepoEntity;
import com.reposcope.backend.repository.HealthCheckResultRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class HealthCheckService {

    private final GitHubApiService gitHubApiService;
    private final HealthCheckResultRepository healthCheckResultRepository;
    private final HealthScoreEngine healthScoreEngine;
    private final ObjectMapper objectMapper;

    public HealthCheckResult analyze(RepoEntity repo) {
        String owner = repo.getOwner();
        String repoName = repo.getRepositoryName();

        JsonNode commits = gitHubApiService.getCommits(owner, repoName);
        JsonNode pullRequests = gitHubApiService.getPullRequests(owner, repoName);
        JsonNode issues = gitHubApiService.getIssues(owner, repoName);
        JsonNode contributors = gitHubApiService.getContributors(owner, repoName);

        Map<String, Double> scores = healthScoreEngine.calculate(
                commits, pullRequests, issues, contributors);

        int healthScore = scores.get("health_score").intValue();

        String healthTree;
        try {
            healthTree = objectMapper.writeValueAsString(scores);
        } catch (Exception e) {
            healthTree = "{}";
        }

        HealthCheckResult result = healthCheckResultRepository
                .findByRepoEntity(repo)
                .orElse(new HealthCheckResult());

        result.setRepoEntity(repo);
        result.setHealthScore(healthScore);
        result.setHealthTree(healthTree);
        result.setAnalyzedAt(LocalDateTime.now());

        return healthCheckResultRepository.save(result);
    }
}