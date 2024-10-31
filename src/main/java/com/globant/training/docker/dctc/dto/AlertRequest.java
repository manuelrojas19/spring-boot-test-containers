package com.globant.training.docker.dctc.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlertRequest {
    private Long userId;
    private String alertMessage;
}
