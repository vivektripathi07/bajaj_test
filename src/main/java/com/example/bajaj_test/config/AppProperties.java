package com.example.bajaj_test.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app")
public class AppProperties {
    private String name;
    private String regNo;
    private String email;
    private String generateWebhookUrl;
    private boolean validateWithH2 = true;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getRegNo() { return regNo; }
    public void setRegNo(String regNo) { this.regNo = regNo; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getGenerateWebhookUrl() { return generateWebhookUrl; }
    public void setGenerateWebhookUrl(String generateWebhookUrl) { this.generateWebhookUrl = generateWebhookUrl; }
    public boolean isValidateWithH2() { return validateWithH2; }
    public void setValidateWithH2(boolean validateWithH2) { this.validateWithH2 = validateWithH2; }
}
