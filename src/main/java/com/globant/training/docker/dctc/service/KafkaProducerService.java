package com.globant.training.docker.dctc.service;

import com.globant.training.docker.dctc.dto.AlertRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class KafkaProducerService {

    private final KafkaTemplate<String, AlertRequest> kafkaTemplate;

    @Autowired
    public KafkaProducerService(KafkaTemplate<String, AlertRequest> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(AlertRequest alertRequest) {
        kafkaTemplate.send("alerts-topic", alertRequest);
    }
}