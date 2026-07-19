package com.reposcope.backend.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.reposcope.backend.engine.IssueDAGEngine;
import com.reposcope.backend.model.IssueRoadmap;
import com.reposcope.backend.model.RepoEntity;
import com.reposcope.backend.repository.IssueRoadmapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.*;

@Service
@RequiredArgsConstructor
public class IssueRoadmapService {

    private final GitHubApiService gitHubApiService;
    private final IssueRoadmapRepository issueRoadmapRepository;
    private final IssueDAGEngine issueDAGEngine;
    private final ObjectMapper objectMapper;

    public IssueRoadmap analyze(RepoEntity repo) {
        JsonNode issues = gitHubApiService.getIssues(
                repo.getOwner(), repo.getRepositoryName());

        Map<Integer, List<Integer>> adjacencyList = new HashMap<>();
        Map<Integer, String> issueTitles = new HashMap<>();

        for (JsonNode issue : issues) {
            if (!issue.path("pull_request").isMissingNode()) continue;

            int issueNumber = issue.path("number").asInt();
            String title = issue.path("title").asText();
            String body = issue.path("body").asText("");

            issueTitles.put(issueNumber, title);
            adjacencyList.putIfAbsent(issueNumber, new ArrayList<>());

            List<Integer> dependencies = parseDependencies(body);
            for (int dep : dependencies) {
                adjacencyList.putIfAbsent(dep, new ArrayList<>());
                adjacencyList.get(dep).add(issueNumber);
            }
        }

        Map<String, Object> dagResult = issueDAGEngine.buildAndSort(
                adjacencyList, issueTitles);

        String issueTree;
        try {
            issueTree = objectMapper.writeValueAsString(dagResult);
        } catch (Exception e) {
            issueTree = "{}";
        }

        IssueRoadmap roadmap = issueRoadmapRepository
                .findByRepoEntity(repo)
                .orElse(new IssueRoadmap());

        roadmap.setRepoEntity(repo);
        roadmap.setIssueTree(issueTree);
        roadmap.setAnalyzedAt(LocalDateTime.now());

        return issueRoadmapRepository.save(roadmap);
    }

    private List<Integer> parseDependencies(String body) {
        List<Integer> deps = new ArrayList<>();
        if (body == null || body.isEmpty()) return deps;
        Pattern pattern = Pattern.compile(
                "(?:blocked by|depends on)\\s*#(\\d+)",
                Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(body);
        while (matcher.find()) {
            deps.add(Integer.parseInt(matcher.group(1)));
        }
        return deps;
    }
}