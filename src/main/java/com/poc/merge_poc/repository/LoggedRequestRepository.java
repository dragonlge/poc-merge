package com.poc.merge_poc.repository;

import com.poc.merge_poc.entity.LoggedRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoggedRequestRepository extends JpaRepository<LoggedRequest, Long> {
    List<LoggedRequest> findByProcessedNullOrProcessedFalse();
}
