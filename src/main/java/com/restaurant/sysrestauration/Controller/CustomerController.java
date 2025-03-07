package com.restaurant.sysrestauration.Controller;

import com.restaurant.sysrestauration.model.Customer;
import com.restaurant.sysrestauration.repository.CustomerRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private CustomerRepository customerRepository;

    @GetMapping
    public List<Customer> getAllClients() {
        return customerRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Customer> getClientById(@PathVariable Long id) {
        return customerRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Customer createClient(@Valid @RequestBody Customer client) {
        return customerRepository.save(client);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Customer> updateClient(@PathVariable Long id, @Valid @RequestBody Customer clientDetails) {
        return customerRepository.findById(id)
                .map(client -> {
                    client.setNom(clientDetails.getNom());
                    client.setEmail(clientDetails.getEmail());
                    client.setAdresse(clientDetails.getAdresse());
                    client.setPassword(clientDetails.getPassword());
                    return ResponseEntity.ok(customerRepository.save(client));
                }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteClient(@PathVariable Long id) {
        return customerRepository.findById(id)
                .map(client -> {
                    customerRepository.delete(client);
                    return ResponseEntity.ok().build();
                }).orElse(ResponseEntity.notFound().build());
    }

}
