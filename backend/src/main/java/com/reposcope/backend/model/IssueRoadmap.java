package com.reposcope.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "issue_roadmap")
public class IssueRoadmap {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "issue_id")
    private UUID issueId;

    @OneToOne
    @JoinColumn(name = "repository_id", nullable = false)
    private RepoEntity repoEntity;

    @Column(name = "issue_tree", columnDefinition = "text")
    private String issueTree;

    @Column(name = "analyzed_at")
    private LocalDateTime analyzedAt;
}