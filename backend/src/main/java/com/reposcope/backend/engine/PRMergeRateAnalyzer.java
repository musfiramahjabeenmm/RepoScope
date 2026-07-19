package com.reposcope.backend.engine;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;

@Component
public class PRMergeRateAnalyzer {

    public double analyze(JsonNode pullRequests) {
        if (pullRequests == null || !pullRequests.isArray()) return 0;
        int total = 0, merged = 0;
        for (JsonNode pr : pullRequests) {
            total++;
            if (!pr.path("merged_at").isNull()) merged++;
        }
        if (total == 0) return 0;
        return ((double) merged / total) * 100;
    }
}