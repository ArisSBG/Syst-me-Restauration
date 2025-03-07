package com.restaurant.sysrestauration.Controller;

import com.restaurant.sysrestauration.model.Dish;
import com.restaurant.sysrestauration.model.Order;
import com.restaurant.sysrestauration.repository.CustomerRepository;
import com.restaurant.sysrestauration.repository.DishRepository;
import com.restaurant.sysrestauration.repository.OrderRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private DishRepository dishRepository;

    @GetMapping
    public List<Order> getAllOrder() {
        return orderRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        return orderRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Order> createOrder(@Valid @RequestBody Order order,
                                             @RequestParam Long clientId,
                                             @RequestParam(required = false) Set<Long> dishIds) {
        return customerRepository.findById(clientId)
                .map(customer -> {
                    order.setClient(customer);
                    if (dishIds != null) {
                        Set<Dish> plats = new HashSet<>(dishRepository.findAllById(dishIds));
                        order.setPlats(plats);
                    }
                    return ResponseEntity.ok(orderRepository.save(order));
                })
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }




    @PutMapping("/{id}")
    public ResponseEntity<Order> updateOrder(@PathVariable Long id,
                                                   @Valid @RequestBody Order commandeDetails,
                                                   @RequestParam Long clientId,
                                                   @RequestParam(required = false) Set<Long> platIds) {
        return orderRepository.findById(id)
                .map(order -> {
                    order.setStatut(commandeDetails.getStatut());
                    order.setPrixTotal(commandeDetails.getPrixTotal());
                    customerRepository.findById(clientId).ifPresent(order::setClient);
                    if (platIds != null) {
                        Set<Dish> plats = new HashSet<>(dishRepository.findAllById(platIds));
                        order.setPlats(plats);
                    }
                    return ResponseEntity.ok(orderRepository.save(order));
                }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCommande(@PathVariable Long id) {
        return orderRepository.findById(id)
                .map(commande -> {
                    orderRepository.delete(commande);
                    return ResponseEntity.ok().build();
                }).orElse(ResponseEntity.notFound().build());
    }

}
