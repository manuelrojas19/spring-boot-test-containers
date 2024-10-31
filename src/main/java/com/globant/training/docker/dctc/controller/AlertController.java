package com.globant.training.docker.dctc.controller;

import com.globant.training.docker.dctc.dto.AlertRequest;
import com.globant.training.docker.dctc.dto.AlertResponse;
import com.globant.training.docker.dctc.service.AlertService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AlertController {

    private final AlertService alertService;

    @PostMapping("/alerts")
    public ResponseEntity<AlertResponse> sendAlert(@RequestBody AlertRequest alertRequest) {
        return ResponseEntity.ok(alertService.sendAlert(alertRequest));
    }
}
