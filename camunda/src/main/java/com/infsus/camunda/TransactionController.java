package com.infsus.camunda;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String CAMUNDA_URL = "http://localhost:8080/engine-rest";

    @PostMapping
    public ResponseEntity<String> startTransaction(@RequestBody Map<String, Object> request) {
        Map<String, Object> payload = Map.of(
                "variables", Map.of(
                        "amount", Map.of("value", request.get("amount"), "type", "Integer")
                )
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Object> entity = new HttpEntity<>(payload, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(
                CAMUNDA_URL + "/process-definition/key/Process_1r056bh/start",
                entity,
                String.class
        );

        return ResponseEntity.ok("Started: " + response.getBody());
    }
}