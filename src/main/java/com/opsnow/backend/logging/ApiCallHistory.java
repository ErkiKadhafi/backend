package com.opsnow.backend.logging;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "api_call_history")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiCallHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime timestamp;
    private String endpoint;
    private String httpMethod;
    private String userIdentifier;
    private int responseStatus;
}
