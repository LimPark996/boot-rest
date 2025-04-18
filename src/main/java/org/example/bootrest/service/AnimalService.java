package org.example.bootrest.service;

import org.example.bootrest.model.domain.Animal;

import java.util.List;

public interface AnimalService {
    List<Animal> findAll();

    void create(Animal animal);
}
