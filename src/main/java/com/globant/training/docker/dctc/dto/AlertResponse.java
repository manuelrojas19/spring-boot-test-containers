package com.globant.training.docker.dctc.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AlertResponse {
    private String alertStatus;
}
