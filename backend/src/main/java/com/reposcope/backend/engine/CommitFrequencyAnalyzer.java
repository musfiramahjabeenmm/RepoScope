package com.reposcope.backend.engine;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

@Component
public class CommitFrequencyAnalyzer {

    public double analyze(JsonNode commits) {
        if (commits == null || !commits.isArray()) return 0;
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime window = now.minusDays(90);
        long[] blocks = new long[9];
        for (JsonNode commit : commits) {
            String dateStr = commit.path("commit").path("author").path("date").asText();
            if (dateStr.isEmpty()) continue;
            LocalDateTime date = ZonedDateTime.parse(dateStr).toLocalDateTime();
            if (date.isAfter(window)) {
                long daysAgo = ChronoUnit.DAYS.between(date, now);
                int block = (int) (daysAgo / 10);
                if (block < 9) blocks[block]++;
            }
        }
        long active = 0;
        for (long b : blocks) if (b > 0) active++;
        return (active / 9.0) * 100;
    }
}