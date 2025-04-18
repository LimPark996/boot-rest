package org.example.bootrest.service;

import org.example.bootrest.model.dto.AnimalRequestDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class GeminiServiceImpl implements GeminiService {
    @Value("${gemini.api-key}")
    private String apiKey;

    @Override
    public String makeStory(AnimalRequestDTO dto) {
        return "";
    }
}
