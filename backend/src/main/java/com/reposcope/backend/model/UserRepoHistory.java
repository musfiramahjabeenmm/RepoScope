package com.reposcope.backend.model;
import com.reposcope.backend.enums.AnalysisStatus;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "user_repo_history")
public class UserRepoHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "history_id")
    private UUID historyId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "repository_id", nullable = false)
    private RepoEntity repository;

    @Column(name = "analyzed_at")
    private LocalDateTime analyzedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "analysis_status")
    private AnalysisStatus analysisStatus;
}