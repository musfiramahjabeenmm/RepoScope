package com.reposcope.backend.repository;

import com.reposcope.backend.model.UserRepoHistory;
import com.reposcope.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface UserRepoHistoryRepository extends JpaRepository<UserRepoHistory, UUID> {
    List<UserRepoHistory> findByUserOrderByAnalyzedAtDesc(User user);
}