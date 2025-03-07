package com.restaurant.sysrestauration.service;

import com.restaurant.sysrestauration.model.Customer;
import com.restaurant.sysrestauration.repository.CustomerRepository;
import com.restaurant.sysrestauration.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public CustomUserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Cas admin statique
        if ("admin@restaurant.com".equalsIgnoreCase(email)) {
            return new CustomUserDetails(email, "{noop}admin", "ROLE_ADMIN");
        }
        // Si l'email contient "@client", on attribue le rôle ROLE_CLIENT
        if (email.contains("@client")) {
            Customer customer = customerRepository.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
            return new CustomUserDetails(customer.getEmail(), "{noop}" + customer.getPassword(), "ROLE_CLIENT");
        }
        // Pour les autres, vous pouvez définir une logique par défaut.
        // Ici, nous attribuons également ROLE_CLIENT par défaut.
        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
        return new CustomUserDetails(customer.getEmail(), "{noop}" + customer.getPassword(), "ROLE_CLIENT");
    }
}
