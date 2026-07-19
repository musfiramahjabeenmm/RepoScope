package com.reposcope.backend.engine;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;

@Component
public class ContributorActivityAnalyzer {

    public double analyze(JsonNode contributors) {
        if (contributors == null || !contributors.isArray()) return 0;
        int count = 0;
        for (JsonNode ignored : contributors) count++;
        return Math.min(count / 10.0, 1.0) * 100;
    }
}