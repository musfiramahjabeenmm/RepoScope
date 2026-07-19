package com.reposcope.backend.engine;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

@Component
public class IssueResponseAnalyzer {

    public double analyze(JsonNode issues) {
        if (issues == null || !issues.isArray()) return 0;
        long totalDays = 0;
        int count = 0;
        for (JsonNode issue : issues) {
            if (!issue.path("pull_request").isMissingNode()) continue;
            String createdAt = issue.path("created_at").asText();
            String updatedAt = issue.path("updated_at").asText();
            if (createdAt.isEmpty() || updatedAt.isEmpty()) continue;
            LocalDateTime created = ZonedDateTime.parse(createdAt).toLocalDateTime();
            LocalDateTime updated = ZonedDateTime.parse(updatedAt).toLocalDateTime();
            totalDays += ChronoUnit.DAYS.between(created, updated);
            count++;
        }
        if (count == 0) return 100;
        double avg = (double) totalDays / count;
        if (avg <= 1) return 100;
        if (avg <= 7) return 80;
        if (avg <= 30) return 50;
        return 20;
    }
}