package com.reposcope.backend.service;

import com.reposcope.backend.enums.AnalysisStatus;
import com.reposcope.backend.model.RepoEntity;
import com.reposcope.backend.repository.RepositoryRepo;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CacheService {

    private final RepositoryRepo repositoryRepo;
    private final GitHubApiService gitHubApiService;

    public RepoEntity getOrCreateRepo(String repositoryUrl, boolean forceRefresh) {
        Optional<RepoEntity> existing = repositoryRepo.findByRepositoryUrl(repositoryUrl);

        if (existing.isPresent()) {
            RepoEntity repo = existing.get();

            if (repo.getAnalysisStatus() == AnalysisStatus.PENDING) {
                return repo;
            }

            if (forceRefresh || isCacheStale(repo)) {
                repo.setAnalysisStatus(AnalysisStatus.PENDING);
                return repositoryRepo.save(repo);
            }

            return repo;
        }

        String[] parts = parseRepoUrl(repositoryUrl);
        String owner = parts[0];
        String repoName = parts[1];

        JsonNode details = gitHubApiService.getRepoDetails(owner, repoName);

        if (details == null || details.get("pushed_at") == null) {
            throw new RuntimeException("Repository not found or inaccessible: " + owner + "/" + repoName);
        }

        RepoEntity repo = new RepoEntity();
        repo.setRepositoryUrl(repositoryUrl);
        repo.setRepositoryName(repoName);
        repo.setOwner(owner);
        repo.setLastPushedAt(parseDate(details.get("pushed_at").asText()));
        repo.setAnalysisStatus(AnalysisStatus.PENDING);
        return repositoryRepo.save(repo);
    }

    private boolean isCacheStale(RepoEntity repo) {
        String[] parts = parseRepoUrl(repo.getRepositoryUrl());
        JsonNode details = gitHubApiService.getRepoDetails(parts[0], parts[1]);
        if (details == null || details.get("pushed_at") == null) return false;
        LocalDateTime githubPushedAt = parseDate(details.get("pushed_at").asText());
        return githubPushedAt.isAfter(repo.getLastPushedAt());
    }

    public String[] parseRepoUrl(String url) {
        String cleaned = url
                .replace("https://github.com/", "")
                .replace("http://github.com/", "")
                .replace("github.com/", "");
        String[] parts = cleaned.split("/");
        return new String[]{parts[0], parts[1]};
    }

    private LocalDateTime parseDate(String dateStr) {
        return ZonedDateTime.parse(dateStr).toLocalDateTime();
    }

}