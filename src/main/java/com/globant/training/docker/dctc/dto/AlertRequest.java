package com.globant.training.docker.dctc.dto;

import lombok.Data;

@Data
public class AlertRequest {
    private Long userId;
    private String alertMessage;
}
