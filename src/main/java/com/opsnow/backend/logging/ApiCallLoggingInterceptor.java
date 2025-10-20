package com.opsnow.backend.logging;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.LocalDateTime;

@Component
public class ApiCallLoggingInterceptor implements HandlerInterceptor {

    private final ApiCallHistoryService apiCallHistoryService;

    public ApiCallLoggingInterceptor(ApiCallHistoryService apiCallHistoryService) {
        this.apiCallHistoryService = apiCallHistoryService;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
            Object handler, Exception ex) {

        ApiCallHistory history = new ApiCallHistory();
        history.setTimestamp(LocalDateTime.now());
        history.setEndpoint(request.getRequestURI());
        history.setHttpMethod(request.getMethod());
        history.setResponseStatus(response.getStatus());
        // history.setUserIdentifier({{USERID}});

        apiCallHistoryService.saveAsync(history);
    }
}
