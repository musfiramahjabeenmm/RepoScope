package com.reposcope.backend.engine;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class HealthScoreEngine {

    private final CommitFrequencyAnalyzer commitFrequencyAnalyzer;
    private final PRMergeRateAnalyzer prMergeRateAnalyzer;
    private final IssueResponseAnalyzer issueResponseAnalyzer;
    private final ContributorActivityAnalyzer contributorActivityAnalyzer;

    public Map<String, Double> calculate(JsonNode commits,
                                         JsonNode pullRequests,
                                         JsonNode issues,
                                         JsonNode contributors) {
        double commitScore = commitFrequencyAnalyzer.analyze(commits);
        double issueScore = issueResponseAnalyzer.analyze(issues);

        int contributorCount = 0;
        for (JsonNode ignored : contributors) contributorCount++;

        double prScore;
        double contributorScore;

        if (contributorCount <= 1) {
            // solo project — don't penalize for no PRs or single contributor
            prScore = commitScore; // use commit consistency as proxy
            contributorScore = 100.0; // solo is fine, not a health issue
        } else {
            prScore = prMergeRateAnalyzer.analyze(pullRequests);
            contributorScore = contributorActivityAnalyzer.analyze(contributors);
        }

        double healthScore = (commitScore * 0.40) +
                (prScore * 0.20) +
                (issueScore * 0.20) +
                (contributorScore * 0.20);

        Map<String, Double> result = new HashMap<>();
        result.put("health_score", healthScore);
        result.put("commit_frequency", commitScore);
        result.put("pr_merge_rate", prScore);
        result.put("issue_response_time", issueScore);
        result.put("contributor_activity", contributorScore);
        result.put("is_solo_project", contributorCount <= 1 ? 1.0 : 0.0);
        return result;
    }
}