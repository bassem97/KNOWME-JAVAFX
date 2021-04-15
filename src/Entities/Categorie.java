package Entities;

import java.util.ArrayList;

public class Categorie {
    private Integer id;
    private String nom;
    private String description;
    private ArrayList<Menu> menus;

    public Categorie() {
        menus = new ArrayList<>();
    }

    public Categorie(String nom, String description) {
        this.nom = nom;
        this.description = description;
        menus = new ArrayList<>();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Categorie{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", description='" + description + '\'' +
                ", menus=" + menus +
                '}';
    }
}
