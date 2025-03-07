package com.restaurant.sysrestauration.service;

import com.restaurant.sysrestauration.model.Dish;
import com.restaurant.sysrestauration.repository.DishRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DishService {

    @Autowired
    private DishRepository dishRepository;

    public List<Dish> getAllPlats() {
        return dishRepository.findAll();
    }

    public Optional<Dish> getPlatById(Long id) {
        return dishRepository.findById(id);
    }

    public Dish createPlat(Dish dish) {
        return dishRepository.save(dish);
    }

    public Optional<Dish> updatePlat(Long id, Dish dishDetails) {
        return dishRepository.findById(id).map(dish -> {
            dish.setNom(dishDetails.getNom());
            dish.setPrix(dishDetails.getPrix());
            dish.setDescription(dishDetails.getDescription());
            dish.setCategorie(dishDetails.getCategorie());
            dish.setAllergenes(dishDetails.getAllergenes());
            dish.setStatut(dishDetails.getStatut());
            return dishRepository.save(dish);
        });
    }

    public boolean deletePlat(Long id) {
        return dishRepository.findById(id).map(dish -> {
            dishRepository.delete(dish);
            return true;
        }).orElse(false);
    }
}
