package com.reposcope.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "health_check_result")
public class HealthCheckResult {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "result_id")
    private UUID resultId;

    @OneToOne
    @JoinColumn(name = "repository_id", nullable = false)
    private RepoEntity repoEntity;

    @Column(name = "health_score")
    private Integer healthScore;

    @Column(name = "health_tree", columnDefinition = "text")
    private String healthTree;

    @Column(name = "analyzed_at")
    private LocalDateTime analyzedAt;
}