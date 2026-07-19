package com.reposcope.backend.repository;

import com.reposcope.backend.model.HealthCheckResult;
import com.reposcope.backend.model.RepoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface HealthCheckResultRepository extends JpaRepository<HealthCheckResult, UUID> {
    Optional<HealthCheckResult> findByRepoEntity(RepoEntity repoEntity);
}