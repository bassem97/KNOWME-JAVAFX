package Entities;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Menu {
    private int id;
    private String name;
    private String description;
    private String img;
    private Boolean is_expired;
    private LocalDateTime creation_date;
    private LocalDateTime expiration_date;
    private Categorie categorie;
    private ArrayList<Ingredient> ingredients;
    private final ImageView imageView;

    public Menu() {
        ingredients = new ArrayList<>();
        creation_date  = LocalDateTime.now();
        expiration_date  = creation_date.plusDays(1);
        is_expired = false;
        imageView = new ImageView();
    }

    public Menu(String name, String description,Categorie categorie) {
        this.name = name;
        this.description = description;
        this.categorie = categorie;
        ingredients = new ArrayList<>();
        creation_date  = LocalDateTime.now();
        expiration_date  = creation_date.plusDays(1);
        is_expired = false;
        imageView = new ImageView();
    }
    public Menu(int id,String name, String description,Categorie categorie) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.categorie = categorie;
        ingredients = new ArrayList<>();
        creation_date  = LocalDateTime.now();
        expiration_date  = creation_date.plusDays(1);
        is_expired = false;
        imageView = new ImageView();
    }
    public Menu(String name, String description) {
        this.name = name;
        this.description = description;
        ingredients = new ArrayList<>();
        creation_date  = LocalDateTime.now();
        expiration_date  = creation_date.plusDays(1);
        is_expired = false;
        imageView = new ImageView();
    }




    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public Boolean getIs_expired() {
        return is_expired;
    }

    public void setIs_expired(Boolean is_expired) {
        this.is_expired = is_expired;
    }

    public LocalDateTime getCreation_date() {
        return creation_date;
    }

    public void setCreation_date(LocalDateTime creation_date) {
        this.creation_date = creation_date;
    }

    public LocalDateTime getExpiration_date() {
        return expiration_date;
    }

    public void setExpiration_date(LocalDateTime expiration_date) {
        this.expiration_date = expiration_date;
    }

    public Categorie getCategorie() {
        return categorie;
    }

    public void setCategorie(Categorie categorie) {
        this.categorie = categorie;
    }

    public ArrayList<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    @Override
    public String toString() {
//        return "Menu{" +
//                "id=" + id +
//                ", name='" + name + '\'' +
//                ", description='" + description + '\'' +
//                ", img='" + img + '\'' +
//                ", is_expired=" + is_expired +
//                ", date=" + creation_date +
//                ", expiration_date=" + expiration_date +
//                ", categorie=" + categorie +
//                '}';
        return name+"";
    }
    public void initializeImageView() {
        File file = new File("src/assets/" + this.img);
        try {
            Image img = new Image(file.toURI().toString());
            imageView.setImage(img);
            imageView.setFitWidth(70);
            imageView.setFitHeight(70);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public ImageView getImageView() {
        return imageView;
    }
}
