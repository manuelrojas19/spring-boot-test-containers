package com.globant.training.docker.dctc.service;

import com.globant.training.docker.dctc.dto.AlertRequest;
import com.globant.training.docker.dctc.dto.AlertResponse;
import com.globant.training.docker.dctc.entity.ApplicationUser;
import com.globant.training.docker.dctc.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlertService {

    private final UserRepository userRepository;
    private final KafkaProducerService kafkaProducerService;

    public AlertResponse sendAlert(AlertRequest alertRequest) {
        log.info("Alert Request {}", alertRequest);

        log.info("Validating if user exists on database {}", alertRequest.getUserId());

        ApplicationUser user = userRepository.findById(alertRequest.getUserId())
                .orElseThrow(() -> {
                    log.warn("User does not exists on DB");
                    return new IllegalArgumentException("User was not found");
                });

        log.info("User found sending alert through kafka");

        kafkaProducerService.sendMessage(alertRequest);

        return AlertResponse.builder()
                .alertStatus("Alert send for user: " + user.getId())
                .build();
    }
}
