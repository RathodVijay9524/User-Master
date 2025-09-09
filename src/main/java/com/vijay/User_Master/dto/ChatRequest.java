package com.vijay.User_Master.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ChatRequest {
    
    @JsonProperty("message")
    private String message;
    
    @JsonProperty("provider")
    private String provider;
    
    @JsonProperty("model")
    private String model;
    
    @JsonProperty("temperature")
    private Double temperature = 0.7;
    
    @JsonProperty("maxTokens")
    private Integer maxTokens = 1000;
    
    @JsonProperty("conversationId")
    private String conversationId;
    
    // API Keys for dynamic provider configuration (optional)
    @JsonProperty("openaiApiKey")
    private String openaiApiKey;
    
    @JsonProperty("claudeApiKey")
    private String claudeApiKey;
    
    @JsonProperty("groqApiKey")
    private String groqApiKey;
    
    @JsonProperty("geminiApiKey")
    private String geminiApiKey;
    
    @JsonProperty("openrouterApiKey")
    private String openrouterApiKey;
    
    @JsonProperty("huggingfaceApiKey")
    private String huggingfaceApiKey;
    
    // Default constructor
    public ChatRequest() {}
    
    // Constructor with parameters
    public ChatRequest(String message, String provider, String model) {
        this.message = message;
        this.provider = provider;
        this.model = model;
    }
    
    // Getters and Setters
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
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
    
    @Override
    public String toString() {
        return "ChatRequest{" +
                "message='" + message + '\'' +
                ", provider='" + provider + '\'' +
                ", model='" + model + '\'' +
                '}';
    }
}
