package org.example.bootrest.service;

import org.example.bootrest.model.domain.Animal;
import org.example.bootrest.model.mapper.AnimalMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnimalServiceImpl implements AnimalService {
    private final AnimalMapper animalMapper;

    public AnimalServiceImpl(AnimalMapper animalMapper) {
        this.animalMapper = animalMapper;
    }

    @Override
    public List<Animal> findAll() {
        return animalMapper.findAll();
    }
}
