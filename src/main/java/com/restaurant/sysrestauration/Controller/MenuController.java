package com.restaurant.sysrestauration.Controller;


import com.restaurant.sysrestauration.model.Dish;
import com.restaurant.sysrestauration.model.Menu;
import com.restaurant.sysrestauration.repository.DishRepository;
import com.restaurant.sysrestauration.repository.MenuRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/menus")
public class MenuController {

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private DishRepository dishRepository;

    @GetMapping
    public List<Menu> getAllMenus() {
        return menuRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Menu> getMenuById(@PathVariable Long id) {
        return menuRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Menu createMenu(@Valid @RequestBody Menu menu, @RequestParam(required = false) Set<Long> platIds) {
        if (platIds != null) {
            Set<Dish> plats = new HashSet<>(dishRepository.findAllById(platIds));
            menu.setPlats(plats);
        }
        return menuRepository.save(menu);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Menu> updateMenu(@PathVariable Long id, @Valid @RequestBody Menu menuDetails,
                                           @RequestParam(required = false) Set<Long> platIds) {
        return menuRepository.findById(id)
                .map(menu -> {
                    menu.setNom(menuDetails.getNom());
                    menu.setDescription(menuDetails.getDescription());
                    menu.setPrix(menuDetails.getPrix());
                    if (platIds != null) {
                        Set<Dish> plats = new HashSet<>(dishRepository.findAllById(platIds));
                        menu.setPlats(plats);
                    }
                    return ResponseEntity.ok(menuRepository.save(menu));
                }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMenu(@PathVariable Long id) {
        return menuRepository.findById(id)
                .map(menu -> {
                    menuRepository.delete(menu);
                    return ResponseEntity.ok().build();
                }).orElse(ResponseEntity.notFound().build());
    }

}


