package com.vijay.User_Master.service;

import com.vijay.User_Master.dto.ChatRequest;
import com.vijay.User_Master.dto.ChatResponse;
import com.vijay.User_Master.dto.ProviderInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;

@Service
public class ChatIntegrationService {
    
    private static final Logger logger = LoggerFactory.getLogger(ChatIntegrationService.class);
    
    private final WebClient webClient;
    
    @Value("${chat.service.base-url:http://localhost:8080}")
    private String chatServiceBaseUrl;
    
    @Value("${chat.service.timeout:30}")
    private int timeoutSeconds;
    
    public ChatIntegrationService() {
        this.webClient = WebClient.builder()
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
    
    /**
     * Send a message to the chat service
     */
    public ChatResponse sendMessage(ChatRequest request) {
        logger.info("Forwarding chat message to chat service: {}", request.getMessage());
        
        try {
            ChatResponse response = webClient.post()
                    .uri(chatServiceBaseUrl + "/api/chat/message")
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(ChatResponse.class)
                    .timeout(Duration.ofSeconds(timeoutSeconds))
                    .block();
            
            logger.info("Successfully received response from chat service");
            return response;
            
        } catch (Exception e) {
            logger.error("Error communicating with chat service: {}", e.getMessage());
            return ChatResponse.builder()
                    .error("Failed to communicate with chat service: " + e.getMessage())
                    .build();
        }
    }
    
    /**
     * Get all available providers from the chat service
     */
    public List<ProviderInfo> getProviders() {
        logger.info("Fetching providers from chat service");
        
        try {
            List<ProviderInfo> providers = webClient.get()
                    .uri(chatServiceBaseUrl + "/api/chat/providers")
                    .retrieve()
                    .bodyToFlux(ProviderInfo.class)
                    .collectList()
                    .timeout(Duration.ofSeconds(timeoutSeconds))
                    .block();
            
            logger.info("Successfully fetched {} providers from chat service", providers.size());
            return providers;
            
        } catch (Exception e) {
            logger.error("Error fetching providers from chat service: {}", e.getMessage());
            return List.of();
        }
    }
    
    /**
     * Get models for a specific provider from the chat service
     */
    public List<String> getModelsForProvider(String providerName) {
        logger.info("Fetching models for provider: {} from chat service", providerName);
        
        try {
            List<String> models = webClient.get()
                    .uri(chatServiceBaseUrl + "/api/chat/providers/" + providerName + "/models")
                    .retrieve()
                    .bodyToFlux(String.class)
                    .collectList()
                    .timeout(Duration.ofSeconds(timeoutSeconds))
                    .block();
            
            logger.info("Successfully fetched {} models for provider {}", models.size(), providerName);
            return models;
            
        } catch (Exception e) {
            logger.error("Error fetching models for provider {} from chat service: {}", providerName, e.getMessage());
            return List.of();
        }
    }
    
    /**
     * Get user's chat list from the chat service
     */
    public List<Object> getUserChatList(String userId) {
        logger.info("Fetching chat list for user: {} from chat service", userId);
        
        try {
            // Parse the response as a List of Objects (conversations)
            List<Object> chats = webClient.get()
                    .uri(chatServiceBaseUrl + "/api/chat/users/" + userId + "/chats")
                    .retrieve()
                    .bodyToMono(List.class)
                    .timeout(Duration.ofSeconds(timeoutSeconds))
                    .onErrorReturn(List.of()) // Return empty list on error
                    .block();
            
            if (chats == null) {
                chats = List.of();
            }
            
            logger.info("Successfully fetched {} chats for user {}", chats.size(), userId);
            return chats;
            
        } catch (Exception e) {
            logger.error("Error fetching chat list for user {} from chat service: {}", userId, e.getMessage());
            return List.of();
        }
    }
    
    /**
     * Get conversation messages from the chat service
     */
    public List<Object> getConversationMessages(String userId, String conversationId) {
        logger.info("Fetching messages for conversation: {} of user: {} from chat service", conversationId, userId);
        
        try {
            // Parse the response as a List of Objects (messages)
            List<Object> messages = webClient.get()
                    .uri(chatServiceBaseUrl + "/api/chat/users/" + userId + "/conversations/" + conversationId + "/messages")
                    .retrieve()
                    .bodyToMono(List.class)
                    .timeout(Duration.ofSeconds(timeoutSeconds))
                    .onErrorReturn(List.of()) // Return empty list on error
                    .block();
            
            if (messages == null) {
                messages = List.of();
            }
            
            logger.info("Successfully fetched {} messages for conversation {}", messages.size(), conversationId);
            return messages;
            
        } catch (Exception e) {
            logger.error("Error fetching messages for conversation {} from chat service: {}", conversationId, e.getMessage());
            return List.of();
        }
    }
    
    /**
     * Get user's chat statistics from the chat service
     */
    public Object getUserChatStats(String userId) {
        logger.info("Fetching chat stats for user: {} from chat service", userId);
        
        try {
            Object stats = webClient.get()
                    .uri(chatServiceBaseUrl + "/api/chat/users/" + userId + "/stats")
                    .retrieve()
                    .bodyToMono(Object.class)
                    .timeout(Duration.ofSeconds(timeoutSeconds))
                    .block();
            
            logger.info("Successfully fetched chat stats for user {}", userId);
            return stats;
            
        } catch (Exception e) {
            logger.error("Error fetching chat stats for user {} from chat service: {}", userId, e.getMessage());
            return null;
        }
    }
    
    // Async versions for better performance
    public Mono<ChatResponse> sendMessageAsync(ChatRequest request) {
        return webClient.post()
                .uri(chatServiceBaseUrl + "/api/chat/message")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(ChatResponse.class)
                .timeout(Duration.ofSeconds(timeoutSeconds));
    }
    
    public Mono<List<ProviderInfo>> getProvidersAsync() {
        return webClient.get()
                .uri(chatServiceBaseUrl + "/api/chat/providers")
                .retrieve()
                .bodyToFlux(ProviderInfo.class)
                .collectList()
                .timeout(Duration.ofSeconds(timeoutSeconds));
    }
    
    public Mono<List<String>> getModelsForProviderAsync(String providerName) {
        return webClient.get()
                .uri(chatServiceBaseUrl + "/api/chat/providers/" + providerName + "/models")
                .retrieve()
                .bodyToFlux(String.class)
                .collectList()
                .timeout(Duration.ofSeconds(timeoutSeconds));
    }
}