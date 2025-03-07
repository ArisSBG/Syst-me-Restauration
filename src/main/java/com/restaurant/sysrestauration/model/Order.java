package com.restaurant.sysrestauration.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime date = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    private StatutCommande statut = StatutCommande.EN_ATTENTE;

    @NotNull
    private Double prixTotal;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Customer customer;

    @ManyToMany
    @JoinTable(name = "order_dishes",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "dish_id"))
    private Set<Dish> dishes;

    public enum StatutCommande {
        EN_ATTENTE,
        EN_PREPARATION,
        PRETE,
        LIVREE
    }

    // Getters et setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }

    public StatutCommande getStatut() { return statut; }
    public void setStatut(StatutCommande statut) { this.statut = statut; }

    public Double getPrixTotal() { return prixTotal; }
    public void setPrixTotal(Double prixTotal) { this.prixTotal = prixTotal; }

    public Customer getClient() { return customer; }
    public void setClient(Customer customer) { this.customer = customer; }

    public Set<Dish> getPlats() { return dishes; }
    public void setPlats(Set<Dish> dishes) { this.dishes = dishes; }

}
