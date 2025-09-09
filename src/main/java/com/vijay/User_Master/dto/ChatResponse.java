package com.vijay.User_Master.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;

public class ChatResponse {
    
    @JsonProperty("response")
    private String response;
    
    @JsonProperty("provider")
    private String provider;
    
    @JsonProperty("model")
    private String model;
    
    @JsonProperty("conversationId")
    private String conversationId;
    
    @JsonProperty("timestamp")
    private LocalDateTime timestamp;
    
    @JsonProperty("tokensUsed")
    private Long tokensUsed;
    
    @JsonProperty("responseTimeMs")
    private Long responseTimeMs;
    
    @JsonProperty("error")
    private String error;
    
    // Default constructor
    public ChatResponse() {}
    
    // Constructor with parameters
    public ChatResponse(String response, String provider, String model, String conversationId) {
        this.response = response;
        this.provider = provider;
        this.model = model;
        this.conversationId = conversationId;
        this.timestamp = LocalDateTime.now();
    }
    
    // Getters and Setters
    public String getResponse() {
        return response;
    }
    
    public void setResponse(String response) {
        this.response = response;
    }
    
    public String getProvider() {
        return provider;
    }
    
    public void setProvider(String provider) {
        this.provider = provider;
    }
    
    public String getModel() {
        return model;
    }
    
    public void setModel(String model) {
        this.model = model;
    }
    
    public String getConversationId() {
        return conversationId;
    }
    
    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
    
    public Long getTokensUsed() {
        return tokensUsed;
    }
    
    public void setTokensUsed(Long tokensUsed) {
        this.tokensUsed = tokensUsed;
    }
    
    public Long getResponseTimeMs() {
        return responseTimeMs;
    }
    
    public void setResponseTimeMs(Long responseTimeMs) {
        this.responseTimeMs = responseTimeMs;
    }
    
    public String getError() {
        return error;
    }
    
    public void setError(String error) {
        this.error = error;
    }
    
    @Override
    public String toString() {
        return "ChatResponse{" +
                "response='" + response + '\'' +
                ", provider='" + provider + '\'' +
                ", model='" + model + '\'' +
                ", conversationId='" + conversationId + '\'' +
                ", timestamp=" + timestamp +
                ", tokensUsed=" + tokensUsed +
                ", responseTimeMs=" + responseTimeMs +
                ", error='" + error + '\'' +
                '}';
    }
}
