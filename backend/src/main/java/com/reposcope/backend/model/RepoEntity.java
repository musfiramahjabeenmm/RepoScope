package com.reposcope.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;
import com.reposcope.backend.enums.AnalysisStatus;

@Data
@Entity
@Table(name = "repository")
public class RepoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "repository_id")
    private UUID repositoryId;

    @Column(name = "repository_name", nullable = false)
    private String repositoryName;

    @Column(name = "repository_url", nullable = false, unique = true)
    private String repositoryUrl;

    @Column(name = "owner", nullable = false)
    private String owner;

    @Column(name = "last_pushed_at")
    private LocalDateTime lastPushedAt;

    @Column(name = "last_analyzed_at")
    private LocalDateTime lastAnalyzedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "analysis_status")
    private AnalysisStatus analysisStatus;
}