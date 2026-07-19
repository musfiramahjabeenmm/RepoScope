package com.reposcope.backend.repository;

import com.reposcope.backend.model.IssueRoadmap;
import com.reposcope.backend.model.RepoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface IssueRoadmapRepository extends JpaRepository<IssueRoadmap, UUID> {
    Optional<IssueRoadmap> findByRepoEntity(RepoEntity repoEntity);
}