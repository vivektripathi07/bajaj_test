package com.example.bajaj_test.service;

import java.time.Duration;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.bajaj_test.config.AppProperties;
import com.example.bajaj_test.model.GenerateRequest;
import com.example.bajaj_test.model.GenerateResponse;

@Service
public class WebhookService {
    private final Logger log = LoggerFactory.getLogger(WebhookService.class);
    private final WebClient webClient;
    private final AppProperties props;

    public WebhookService(WebClient webClient, AppProperties props) {
        this.webClient = webClient;
        this.props = props;
    }

    public GenerateResponse generateWebhook(GenerateRequest req) {
        log.info("Calling generateWebhook: {}", props.getGenerateWebhookUrl());
        return webClient.post()
                .uri(props.getGenerateWebhookUrl())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(req)
                .retrieve()
                .bodyToMono(GenerateResponse.class)
                .timeout(Duration.ofSeconds(20))
                .block();
    }

    public void submitFinalQuery(String webhookUrl, String accessToken, String finalQuery) {
        log.info("Submitting final query to: {}", webhookUrl);
        Map<String,String> payload = Map.of("finalQuery", finalQuery);

        webClient.post()
                .uri(webhookUrl)
                .header("Authorization", accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(payload)
                .retrieve()
                .toBodilessEntity()
                .timeout(Duration.ofSeconds(20))
                .block();
    }
}
