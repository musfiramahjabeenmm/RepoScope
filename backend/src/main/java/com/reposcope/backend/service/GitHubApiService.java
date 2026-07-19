package com.reposcope.backend.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class GitHubApiService {

    @Value("${github.api.base-url}")
    private String baseUrl;

    @Value("${github.api.token}")
    private String token;

    private final ObjectMapper objectMapper;

    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        headers.set("Accept", "application/vnd.github.v3+json");
        return headers;
    }

    private JsonNode fetch(String url) {
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> entity = new HttpEntity<>(getHeaders());
        ResponseEntity<String> response = restTemplate.exchange(
                url, HttpMethod.GET, entity, String.class);
        try {
            return objectMapper.readTree(response.getBody());
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse GitHub API response: " + e.getMessage());
        }
    }

    public JsonNode getRepoDetails(String owner, String repo) {
        return fetch(baseUrl + "/repos/" + owner + "/" + repo);
    }

    public JsonNode getCommits(String owner, String repo) {
        return fetch(baseUrl + "/repos/" + owner + "/" + repo + "/commits?per_page=30");
    }

    public JsonNode getPullRequests(String owner, String repo) {
        return fetch(baseUrl + "/repos/" + owner + "/" + repo + "/pulls?state=all&per_page=30");
    }

    public JsonNode getIssues(String owner, String repo) {
        return fetch(baseUrl + "/repos/" + owner + "/" + repo + "/issues?state=all&per_page=30");
    }

    public JsonNode getContributors(String owner, String repo) {
        return fetch(baseUrl + "/repos/" + owner + "/" + repo + "/contributors?per_page=30");
    }
}