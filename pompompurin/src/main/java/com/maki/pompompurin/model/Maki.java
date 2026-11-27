package com.maki.pompompurin.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

// @Entity: cada objeto Maki se guarda como una fila de la tabla "makis"
@Entity
@Table(name = "makis")
public class Maki {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // la BD genera el id (1, 2, 3...)
    private Long id;
    private String name;
    private double price;
    private int pieces;
    private String ingredients;
    private double costo;

    // JPA necesita un constructor vacío
    public Maki() {
    }

    public Maki(String name, double price, int pieces, String ingredients, double costo) {
        this.name = name;
        this.price = price;
        this.pieces = pieces;
        this.ingredients = ingredients;
        this.costo = costo;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public int getPieces() { return pieces; }
    public void setPieces(int pieces) { this.pieces = pieces; }

    public String getIngredients() { return ingredients; }
    public void setIngredients(String ingredients) { this.ingredients = ingredients; }

    public double getCosto() { return price; }
    public void setCosto(double price) { this.price = price; }
}
