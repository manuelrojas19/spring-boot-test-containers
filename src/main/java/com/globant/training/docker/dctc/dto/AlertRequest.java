package com.globant.training.docker.dctc.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AlertRequest {
    private Long userId;
    private String alertMessage;
}
