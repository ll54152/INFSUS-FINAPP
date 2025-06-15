package com.infsus.camunda;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/process")
public class ProcessController {

    private final RestTemplate restTemplate;

    public ProcessController(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }

    private final String camundaUrl = "http://localhost:8080/engine-rest";
    private final String processDefinitionKey = "Process_1r056bh";

    private Map<String, Object> prepareVariables(Map<String, Object> variables) {
        Map<String, Object> vars = new HashMap<>();
        variables.forEach((k, v) -> {
            String type;
            if (v instanceof Integer) {
                type = "Integer";
            } else if (v instanceof Boolean) {
                type = "Boolean";
            } else if (v instanceof Double || v instanceof Float) {
                type = "Double";
            } else {
                type = "String"; // default
            }
            Map<String, Object> valueType = Map.of("value", v, "type", type);
            vars.put(k, valueType);
        });
        return vars;
    }

    // stari start
//    @PostMapping("/start")
//    public ResponseEntity<String> startProcess(@RequestBody Map<String, Object> variables) {
//        Map<String, Object> vars = prepareVariables(variables);
//        Map<String, Object> body = Map.of("variables", vars);
//
//        ResponseEntity<String> response = restTemplate.postForEntity(
//                camundaUrl + "/process-definition/key/" + processDefinitionKey + "/start",
//                body,
//                String.class
//        );
//        return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
//    }

    @PostMapping("/start")
    public ResponseEntity<String> startProcess(@RequestBody Map<String, Object> variables) {
        Map<String, Object> vars = new HashMap<>();

        variables.forEach((key, value) -> {
            String type;
            if (value instanceof Integer) type = "Integer";
            else if (value instanceof Boolean) type = "Boolean";
            else if (value instanceof Double || value instanceof Float) type = "Double";
            else type = "String";

            vars.put(key, Map.of("value", value, "type", type));
        });

        Map<String, Object> body = Map.of("variables", vars);

        ResponseEntity<String> response = restTemplate.postForEntity(
                camundaUrl + "/process-definition/key/Process_1r056bh/start",
                body,
                String.class
        );
        return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
    }


//    @GetMapping("/tasks")
//    public ResponseEntity<String> getAllTasks() {
//        String url = camundaUrl + "/task";
//        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
//        return ResponseEntity.ok(response.getBody());
//    }

    @GetMapping("/tasks")
    public ResponseEntity<String> getTasks() {
        String url = camundaUrl + "/task?processDefinitionKey=Process_1r056bh&includeProcessVariables=true";

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        return ResponseEntity.ok(response.getBody());
    }

    @GetMapping("/tasksWithVariables")
    public ResponseEntity<List<Map<String, Object>>> getTasksWithVariables() {
        String tasksUrl = camundaUrl + "/task?processDefinitionKey=Process_1r056bh";
        ResponseEntity<List<Map<String, Object>>> tasksResponse =
                restTemplate.exchange(tasksUrl, HttpMethod.GET, null,
                        new ParameterizedTypeReference<>() {
                        });

        List<Map<String, Object>> tasks = tasksResponse.getBody();

        for (Map<String, Object> task : tasks) {
            String processInstanceId = (String) task.get("processInstanceId");
            String variablesUrl = camundaUrl + "/process-instance/" + processInstanceId + "/variables";

            ResponseEntity<Map<String, Map<String, Object>>> varsResponse =
                    restTemplate.exchange(variablesUrl, HttpMethod.GET, null,
                            new ParameterizedTypeReference<>() {
                            });

            task.put("variables", varsResponse.getBody());
        }

        return ResponseEntity.ok(tasks);
    }




    // stari
//    @PostMapping("/tasks/{taskId}/complete")
//    public ResponseEntity<String> completeTask(@PathVariable String taskId, @RequestBody Map<String, Object> variables) {
//        Map<String, Object> vars = prepareVariables(variables);
//        Map<String, Object> body = Map.of("variables", vars);
//
//        restTemplate.postForEntity(camundaUrl + "/task/" + taskId + "/complete", body, String.class);
//        return ResponseEntity.ok("Task completed");
//    }

    @PostMapping("/tasks/{taskId}/complete")
    public ResponseEntity<String> completeTask(@PathVariable String taskId, @RequestBody Map<String, Object> body) {
        Object variablesObj = body.get("variables");
        if (!(variablesObj instanceof Map)) {
            return ResponseEntity.badRequest().body("Missing or invalid 'variables' field");
        }
        Map<String, Object> variables = (Map<String, Object>) variablesObj;

        Map<String, Object> bodyToCamunda = Map.of("variables", variables);

        restTemplate.postForEntity(camundaUrl + "/task/" + taskId + "/complete", bodyToCamunda, String.class);
        return ResponseEntity.ok("Task completed");
    }




    @GetMapping("/instances/{instanceId}/variables")
    public ResponseEntity<String> getProcessVariables(@PathVariable String instanceId) {
        String url = camundaUrl + "/process-instance/" + instanceId + "/variables";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        return ResponseEntity.ok(response.getBody());
    }

//    @GetMapping("/tasks/{taskId}/variables")
//    public ResponseEntity<String> getTaskVariables(@PathVariable String taskId) {
//        String url = camundaUrl + "/task/" + taskId + "/variables";
//        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
//        return ResponseEntity.ok(response.getBody());
//    }


    @GetMapping("/tasks/{taskId}/variables")
    public ResponseEntity<Map<String, Object>> getTaskVariables(@PathVariable String taskId) {
        ResponseEntity<Map> response = restTemplate.getForEntity(
                camundaUrl + "/task/" + taskId + "/variables",
                Map.class
        );
        return ResponseEntity.ok(response.getBody());
    }
}