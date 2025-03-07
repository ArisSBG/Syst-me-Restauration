package com.restaurant.sysrestauration.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

@Entity
public class Dish {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank
    private String nom;

    @NotNull
    private Double prix;

    @Lob
    private String description;

    private String categorie;

    // Stocke les allergènes sous forme de chaîne (par exemple, "lactose, gluten")
    private String allergenes;

    @Enumerated(EnumType.STRING)
    private StatutPlat statut = StatutPlat.disponible;

    @ManyToMany(mappedBy = "dishes")
    private Set<Menu> menus;

    @ManyToMany(mappedBy = "dishes")
    private Set<Order> orders;

    public enum StatutPlat {
        disponible,
        non_disponible
    }

    // Getters et setters

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public Double getPrix() { return prix; }
    public void setPrix(Double prix) { this.prix = prix; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCategorie() { return categorie; }
    public void setCategorie(String categorie) { this.categorie = categorie; }

    public String getAllergenes() { return allergenes; }
    public void setAllergenes(String allergenes) { this.allergenes = allergenes; }

    public StatutPlat getStatut() { return statut; }
    public void setStatut(StatutPlat statut) { this.statut = statut; }
}
