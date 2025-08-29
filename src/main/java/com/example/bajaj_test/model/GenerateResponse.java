package com.example.bajaj_test.model;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnySetter;

public class GenerateResponse {
    private String webhook;
    private String webhookUrl;
    private String accessToken;
    private Map<String, Object> extra = new HashMap<>();

    public String getWebhook() { return webhook; }
    public void setWebhook(String webhook) { this.webhook = webhook; }
    public String getWebhookUrl() { return webhookUrl; }
    public void setWebhookUrl(String webhookUrl) { this.webhookUrl = webhookUrl; }
    public String getAccessToken() { return accessToken; }
    public void setAccessToken(String accessToken) { this.accessToken = accessToken; }

    @JsonAnySetter
    public void setExtra(String key, Object value) { extra.put(key, value); }

    public String resolvedWebhook() {
        if (webhookUrl != null && !webhookUrl.isBlank()) return webhookUrl;
        if (webhook != null && !webhook.isBlank()) return webhook;
        Object alt = extra.get("webhook");
        if (alt instanceof String) return (String) alt;
        alt = extra.get("webhookUrl");
        if (alt instanceof String) return (String) alt;
        return null;
    }
}
