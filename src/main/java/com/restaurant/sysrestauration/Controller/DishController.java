package com.restaurant.sysrestauration.Controller;

import com.restaurant.sysrestauration.model.Dish;
import com.restaurant.sysrestauration.repository.DishRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dishes")
public class DishController {

    @Autowired
    private DishRepository dishRepository;

    @GetMapping
    public List<Dish> getAllPlats() {
        return dishRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Dish> getPlatById(@PathVariable Long id) {
        return dishRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Dish createPlat(@Valid @RequestBody Dish dish) {
        return dishRepository.save(dish);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Dish> updatePlat(@PathVariable Long id, @Valid @RequestBody Dish dishDetails) {
        return dishRepository.findById(id)
                .map(dish -> {
                    dish.setNom(dishDetails.getNom());
                    dish.setPrix(dishDetails.getPrix());
                    dish.setDescription(dishDetails.getDescription());
                    dish.setCategorie(dishDetails.getCategorie());
                    dish.setAllergenes(dishDetails.getAllergenes());
                    dish.setStatut(dishDetails.getStatut());
                    return ResponseEntity.ok(dishRepository.save(dish));
                }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePlat(@PathVariable Long id) {
        return dishRepository.findById(id)
                .map(dish -> {
                    dishRepository.delete(dish);
                    return ResponseEntity.ok().build();
                }).orElse(ResponseEntity.notFound().build());
    }

}
