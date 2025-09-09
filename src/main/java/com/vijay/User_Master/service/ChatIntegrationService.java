package com.vijay.User_Master.service;

import com.vijay.User_Master.dto.ChatRequest;
import com.vijay.User_Master.dto.ChatResponse;
import com.vijay.User_Master.dto.ProviderInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;

@Service
public class ChatIntegrationService {
    
    private static final Logger logger = LoggerFactory.getLogger(ChatIntegrationService.class);
    
    private final WebClient chatServiceWebClient;
    
    @Autowired
    public ChatIntegrationService(WebClient chatServiceWebClient) {
        this.chatServiceWebClient = chatServiceWebClient;
    }
    
    /**
     * Send a message to the chat service
     * @param chatRequest The chat request containing message, provider, and model
     * @return ChatResponse from the chat service
     */
    public ChatResponse sendMessage(ChatRequest chatRequest) {
        logger.info("Sending message to chat service: {}", chatRequest);
        
        try {
            return chatServiceWebClient
                    .post()
                    .uri("/api/chat/message")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(chatRequest)
                    .retrieve()
                    .bodyToMono(ChatResponse.class)
                    .doOnSuccess(response -> logger.info("Successfully received response from chat service"))
                    .doOnError(error -> logger.error("Error calling chat service: {}", error.getMessage()))
                    .block();
                    
        } catch (WebClientResponseException e) {
            logger.error("HTTP error calling chat service: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new RuntimeException("Failed to send message to chat service: " + e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Unexpected error calling chat service: {}", e.getMessage());
            throw new RuntimeException("Failed to send message to chat service: " + e.getMessage(), e);
        }
    }
    
    /**
     * Get all available providers from the chat service
     * @return List of ProviderInfo
     */
    public List<ProviderInfo> getProviders() {
        logger.info("Fetching providers from chat service");
        
        try {
            return chatServiceWebClient
                    .get()
                    .uri("/api/chat/providers")
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<List<ProviderInfo>>() {})
                    .doOnSuccess(providers -> logger.info("Successfully fetched {} providers", providers.size()))
                    .doOnError(error -> logger.error("Error fetching providers: {}", error.getMessage()))
                    .block();
                    
        } catch (WebClientResponseException e) {
            logger.error("HTTP error fetching providers: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new RuntimeException("Failed to fetch providers from chat service: " + e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Unexpected error fetching providers: {}", e.getMessage());
            throw new RuntimeException("Failed to fetch providers from chat service: " + e.getMessage(), e);
        }
    }
    
    /**
     * Get models for a specific provider from the chat service
     * @param providerName The name of the provider
     * @return List of model names
     */
    public List<String> getModelsForProvider(String providerName) {
        logger.info("Fetching models for provider: {}", providerName);
        
        try {
            return chatServiceWebClient
                    .get()
                    .uri("/api/chat/providers/{providerName}/models", providerName)
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<List<String>>() {})
                    .doOnSuccess(models -> logger.info("Successfully fetched {} models for provider {}", models.size(), providerName))
                    .doOnError(error -> logger.error("Error fetching models for provider {}: {}", providerName, error.getMessage()))
                    .block();
                    
        } catch (WebClientResponseException e) {
            logger.error("HTTP error fetching models for provider {}: {} - {}", providerName, e.getStatusCode(), e.getResponseBodyAsString());
            throw new RuntimeException("Failed to fetch models for provider " + providerName + ": " + e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Unexpected error fetching models for provider {}: {}", providerName, e.getMessage());
            throw new RuntimeException("Failed to fetch models for provider " + providerName + ": " + e.getMessage(), e);
        }
    }
    
    /**
     * Async version of sendMessage for non-blocking operations
     * @param chatRequest The chat request
     * @return Mono<ChatResponse>
     */
    public Mono<ChatResponse> sendMessageAsync(ChatRequest chatRequest) {
        logger.info("Sending message to chat service asynchronously: {}", chatRequest);
        
        return chatServiceWebClient
                .post()
                .uri("/api/chat/message")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(chatRequest)
                .retrieve()
                .bodyToMono(ChatResponse.class)
                .doOnSuccess(response -> logger.info("Successfully received async response from chat service"))
                .doOnError(error -> logger.error("Error in async call to chat service: {}", error.getMessage()));
    }
    
    /**
     * Async version of getProviders for non-blocking operations
     * @return Mono<List<ProviderInfo>>
     */
    public Mono<List<ProviderInfo>> getProvidersAsync() {
        logger.info("Fetching providers from chat service asynchronously");
        
        return chatServiceWebClient
                .get()
                .uri("/api/chat/providers")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<ProviderInfo>>() {})
                .doOnSuccess(providers -> logger.info("Successfully fetched {} providers asynchronously", providers.size()))
                .doOnError(error -> logger.error("Error fetching providers asynchronously: {}", error.getMessage()));
    }
    
    /**
     * Async version of getModelsForProvider for non-blocking operations
     * @param providerName The name of the provider
     * @return Mono<List<String>>
     */
    public Mono<List<String>> getModelsForProviderAsync(String providerName) {
        logger.info("Fetching models for provider asynchronously: {}", providerName);
        
        return chatServiceWebClient
                .get()
                .uri("/api/chat/providers/{providerName}/models", providerName)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<String>>() {})
                .doOnSuccess(models -> logger.info("Successfully fetched {} models for provider {} asynchronously", models.size(), providerName))
                .doOnError(error -> logger.error("Error fetching models for provider {} asynchronously: {}", providerName, error.getMessage()));
    }
}
