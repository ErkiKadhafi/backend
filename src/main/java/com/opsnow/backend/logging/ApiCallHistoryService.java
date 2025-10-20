package com.opsnow.backend.logging;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class ApiCallHistoryService {

    private final ApiCallHistoryRepository apiCallHistoryRepository;

    public ApiCallHistoryService(ApiCallHistoryRepository apiCallHistoryRepository) {
        this.apiCallHistoryRepository = apiCallHistoryRepository;

    }

    @Async
    public void saveAsync(ApiCallHistory history) {
        apiCallHistoryRepository.save(history);
    }
}