package com.krishiconnecthub.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.krishiconnecthub.dto.AdvisoryRequestDTO;
import com.krishiconnecthub.model.AdvisoryRequest;
import com.krishiconnecthub.model.User;
import com.krishiconnecthub.repository.AdvisoryRequestRepository;
import com.krishiconnecthub.repository.UserRepository;
import com.krishiconnecthub.service.AdvisoryService;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * The real implementation of the AdvisoryService that calls the Groq API.
 * This service is active only when the 'prod' Spring profile is enabled.
 */
@Service
@Profile("prod")
public class AdvisoryServiceImpl implements AdvisoryService {

    private static final Logger logger = LoggerFactory.getLogger(AdvisoryServiceImpl.class);
    // Groq API endpoint, which is OpenAI-compatible
    private static final String GROQ_API_URL = "https://api.groq.com/openai/v1/chat/completions";
    // A fast and capable model available on Groq
    private static final String GROQ_MODEL = "llama3-8b-8192";

    private final AdvisoryRequestRepository advisoryRequestRepository;
    private final UserRepository userRepository;
    private final CloseableHttpClient httpClient;
    private final ObjectMapper objectMapper;

    // Updated to use the new property from application.properties
    @Value("${groq.api.key}")
    private String groqApiKey;

    public AdvisoryServiceImpl(AdvisoryRequestRepository advisoryRequestRepository, UserRepository userRepository) {
        this.advisoryRequestRepository = advisoryRequestRepository;
        this.userRepository = userRepository;
        this.httpClient = HttpClients.createDefault();
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public AdvisoryRequest getAdvisoryAndSave(AdvisoryRequestDTO requestDTO) throws IOException {
        logger.info("Using real Groq AdvisoryService because 'prod' profile is active.");
        User farmer = userRepository.findById(requestDTO.getFarmerId())
            .orElseThrow(() -> new IllegalArgumentException("Farmer not found with ID: " + requestDTO.getFarmerId()));

        String suggestion = getSuggestionFromGroq(requestDTO.getCropType(), requestDTO.getSoilType(), requestDTO.getSeason());

        AdvisoryRequest advisoryRequest = new AdvisoryRequest();
        advisoryRequest.setFarmer(farmer);
        advisoryRequest.setCropType(requestDTO.getCropType());
        advisoryRequest.setSoilType(requestDTO.getSoilType());
        advisoryRequest.setSeason(requestDTO.getSeason());
        advisoryRequest.setSuggestion(suggestion);

        return advisoryRequestRepository.save(advisoryRequest);
    }

    private String getSuggestionFromGroq(String crop, String soil, String season) throws IOException {
        if (groqApiKey == null || groqApiKey.equals("YOUR_GROQ_API_KEY_IS_NOT_SET") || groqApiKey.trim().isEmpty()) {
            String warningMsg = "Groq API key is not configured. Please set the GROQ_API_KEY environment variable.";
            logger.warn(warningMsg);
            throw new IOException(warningMsg);
        }

        String prompt = String.format(
            "You are an expert agricultural advisor. Provide practical, step-by-step farming advice for a farmer growing '%s' in '%s' soil during the '%s' season. Focus on actionable tips.",
            crop, soil, season
        );

        HttpPost post = new HttpPost(GROQ_API_URL);
        // Groq uses a Bearer token for authorization, which is different from Gemini's API key in the URL.
        post.addHeader("Authorization", "Bearer " + groqApiKey);
        post.addHeader("Content-Type", "application/json");

        // The JSON payload for Groq (OpenAI-compatible) is different from Gemini's.
        String jsonPayload = String.format(
            "{\"messages\":[{\"role\":\"user\",\"content\":\"%s\"}],\"model\":\"%s\"}",
            prompt, GROQ_MODEL
        );
        post.setEntity(new StringEntity(jsonPayload));

        logger.info("Sending request to Groq API for crop: {}. Model: {}", crop, GROQ_MODEL);
        try (CloseableHttpResponse response = httpClient.execute(post)) {
            String responseBody = EntityUtils.toString(response.getEntity());
            int statusCode = response.getStatusLine().getStatusCode();

            if (statusCode != 200) {
                logger.error("Error from Groq API. Status: {}, Body: {}", statusCode, responseBody);
                throw new IOException("Failed to get suggestion from Groq API. Status code: " + statusCode);
            }

            logger.info("Received successful response from Groq API.");
            JsonNode rootNode = objectMapper.readTree(responseBody);
            // The path to the content in the response JSON is different for Groq.
            JsonNode textNode = rootNode.at("/choices/0/message/content");
            
            if (textNode.isMissingNode()) {
                logger.error("Could not parse suggestion text from Groq response: {}", responseBody);
                throw new IOException("Invalid response format from Groq API.");
            }
            
            return textNode.asText();
        }
    }
}