package com.reposcope.backend.controller;

import com.reposcope.backend.dto.response.HistoryResponse;
import com.reposcope.backend.service.RepoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/history")
@RequiredArgsConstructor
public class HistoryController {

    private final RepoService repoService;

    @GetMapping
    public ResponseEntity<List<HistoryResponse>> getHistory(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(
                repoService.getHistory(userDetails.getUsername()));
    }

    @DeleteMapping("/{historyId}")
    public ResponseEntity<String> deleteHistory(
            @PathVariable String historyId) {
        repoService.deleteHistory(historyId);
        return ResponseEntity.ok("History deleted successfully");
    }
}