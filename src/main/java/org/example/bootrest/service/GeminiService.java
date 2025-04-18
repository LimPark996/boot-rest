package org.example.bootrest.service;

import org.example.bootrest.model.dto.AnimalRequestDTO;

public interface GeminiService {
    String makeStory(AnimalRequestDTO dto);
}
