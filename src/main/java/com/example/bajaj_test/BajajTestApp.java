package com.example.bajaj_test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.example.bajaj_test.config.AppProperties;
import com.example.bajaj_test.model.GenerateRequest;
import com.example.bajaj_test.model.GenerateResponse;
import com.example.bajaj_test.service.H2ValidatorService;
import com.example.bajaj_test.service.SQLService;
import com.example.bajaj_test.service.WebhookService;

@SpringBootApplication
@EnableConfigurationProperties
public class BajajTestApp implements CommandLineRunner {
    private final Logger log = LoggerFactory.getLogger(BajajTestApp.class);

    private final AppProperties props;
    private final WebhookService webhookService;
    private final SQLService sqlService;
    private final H2ValidatorService h2ValidatorService;

    public BajajTestApp(AppProperties props,
                        WebhookService webhookService,
                        SQLService sqlService,
                        H2ValidatorService h2ValidatorService) {
        this.props = props;
        this.webhookService = webhookService;
        this.sqlService = sqlService;
        this.h2ValidatorService = h2ValidatorService;
    }

    public static void main(String[] args) {
        SpringApplication.run(BajajTestApp.class, args);
    }

    @Override
    public void run(String... args) {
        log.info("Starting Bajaj Test App...");
        try {
            GenerateRequest req = new GenerateRequest(props.getName(), props.getRegNo(), props.getEmail());
            GenerateResponse resp = webhookService.generateWebhook(req);

            if (resp == null || resp.resolvedWebhook() == null || resp.getAccessToken() == null) {
                log.error("Invalid response from generateWebhook. Aborting.");
                System.exit(1);
            }

            String webhookUrl = resp.resolvedWebhook();
            String token = resp.getAccessToken();

            String finalQuery = sqlService.getFinalQuery(props.getRegNo());
            log.info("Prepared final SQL:\n{}", finalQuery);

            if (props.isValidateWithH2()) {
                h2ValidatorService.validateIfAvailable(finalQuery);
            }

            webhookService.submitFinalQuery(webhookUrl, token, finalQuery);
            log.info("Done. Exiting.");
            System.exit(0);
        } catch (Exception ex) {
            log.error("Error: {}", ex.getMessage(), ex);
            System.exit(2);
        }
    }
}
