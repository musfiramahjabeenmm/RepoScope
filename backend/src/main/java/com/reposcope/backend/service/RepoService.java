package com.reposcope.backend.service;

import com.reposcope.backend.dto.response.AnalyzeRepoResponse;
import com.reposcope.backend.dto.response.HealthCheckResponse;
import com.reposcope.backend.dto.response.HistoryResponse;
import com.reposcope.backend.dto.response.IssueRoadmapResponse;
import com.reposcope.backend.enums.AnalysisStatus;
import com.reposcope.backend.model.*;
import com.reposcope.backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RepoService {

    private final CacheService cacheService;
    private final HealthCheckService healthCheckService;
    private final IssueRoadmapService issueRoadmapService;
    private final UserRepository userRepository;
    private final RepositoryRepo repositoryRepo;
    private final UserRepoHistoryRepository userRepoHistoryRepository;
    private final HealthCheckResultRepository healthCheckResultRepository;
    private final IssueRoadmapRepository issueRoadmapRepository;

    public AnalyzeRepoResponse analyze(String repoUrl, String userEmail, boolean forceRefresh) {
        User user = userRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        RepoEntity repo = cacheService.getOrCreateRepo(repoUrl, forceRefresh);

        if (repo.getAnalysisStatus() == AnalysisStatus.PENDING) {
            HealthCheckResult healthResult = healthCheckService.analyze(repo);

            IssueRoadmap roadmap = null;
            if (healthResult.getHealthScore() < 50) {
                roadmap = issueRoadmapService.analyze(repo);
            }

            repo.setAnalysisStatus(AnalysisStatus.COMPLETE);
            repo.setLastAnalyzedAt(java.time.LocalDateTime.now());
            repositoryRepo.save(repo);

            saveHistory(user, repo);

            return buildResponse(repo, healthResult, roadmap);
        }

        HealthCheckResult healthResult = healthCheckResultRepository
                .findByRepoEntity(repo)
                .orElseThrow(() -> new RuntimeException("Health check not found"));

        IssueRoadmap roadmap = issueRoadmapRepository
                .findByRepoEntity(repo)
                .orElse(null);

        saveHistory(user, repo);

        return buildResponse(repo, healthResult, roadmap);
    }

    public HealthCheckResponse getHealth(String repositoryId) {
        RepoEntity repo = repositoryRepo.findById(
                        java.util.UUID.fromString(repositoryId))
                .orElseThrow(() -> new RuntimeException("Repository not found"));

        HealthCheckResult result = healthCheckResultRepository
                .findByRepoEntity(repo)
                .orElseThrow(() -> new RuntimeException("Health check not found"));

        return new HealthCheckResponse(
                repo.getRepositoryName(),
                repo.getOwner(),
                result.getHealthScore(),
                null,
                result.getAnalyzedAt().toString()
        );
    }

    public IssueRoadmapResponse getIssueRoadmap(String repositoryId) {
        RepoEntity repo = repositoryRepo.findById(
                        java.util.UUID.fromString(repositoryId))
                .orElseThrow(() -> new RuntimeException("Repository not found"));

        IssueRoadmap roadmap = issueRoadmapRepository
                .findByRepoEntity(repo)
                .orElseThrow(() -> new RuntimeException("Issue roadmap not found"));

        return new IssueRoadmapResponse(
                repo.getRepositoryName(),
                roadmap.getIssueTree(),
                roadmap.getAnalyzedAt().toString()
        );
    }

    public List<HistoryResponse> getHistory(String userEmail) {
        User user = userRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return userRepoHistoryRepository
                .findByUserOrderByAnalyzedAtDesc(user)
                .stream()
                .map(h -> new HistoryResponse(
                        h.getHistoryId().toString(),
                        h.getRepository().getRepositoryName(),
                        h.getRepository().getRepositoryUrl(),
                        h.getAnalyzedAt().toString()
                ))
                .collect(Collectors.toList());
    }

    public void deleteHistory(String historyId) {
        userRepoHistoryRepository.deleteById(
                java.util.UUID.fromString(historyId));
    }

    private void saveHistory(User user, RepoEntity repo) {
        UserRepoHistory history = new UserRepoHistory();
        history.setUser(user);
        history.setRepository(repo);
        history.setAnalyzedAt(java.time.LocalDateTime.now());
        userRepoHistoryRepository.save(history);
    }
    private AnalyzeRepoResponse buildResponse(RepoEntity repo, HealthCheckResult health, IssueRoadmap roadmap) {
        Map<String, Double> breakdown = null;
        try {
            breakdown = new com.fasterxml.jackson.databind.ObjectMapper()
                    .readValue(health.getHealthTree(),
                            new com.fasterxml.jackson.core.type.TypeReference<>() {});
        } catch (Exception e) {
            breakdown = new java.util.HashMap<>();
        }

        boolean isSolo = breakdown != null &&
                breakdown.getOrDefault("is_solo_project", 0.0) == 1.0;

        return new AnalyzeRepoResponse(
                repo.getRepositoryName(),
                repo.getOwner(),
                health.getHealthScore(),
                breakdown,
                roadmap != null ? roadmap.getIssueTree() : null,
                repo.getAnalysisStatus().toString(),
                isSolo
        );
    }
}