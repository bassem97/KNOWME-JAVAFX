package Entities;

import com.jfoenix.controls.JFXCheckBox;

public class Ingredient {
    private Integer id;
    private String description;
    private Menu menu;
    private JFXCheckBox checkBox;

    public Ingredient() {
        this.checkBox = new JFXCheckBox();
        checkBox.setText(description);
        checkBox.setMinSize(50,50);
    }

    public Ingredient(String description,Menu menu) {
        this.description = description;
        this.menu = menu;
        this.checkBox = new JFXCheckBox();
        checkBox.setText(description);
        checkBox.setMinSize(50,50);
    }

    public Ingredient(Integer id, String description, Menu menu) {
        this.id = id;
        this.description = description;
        this.menu = menu;
        this.checkBox = new JFXCheckBox();
        checkBox.setText(description);
        checkBox.setMinSize(50,50);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    @Override
    public String toString() {
//        return "Ingredient{" +
//                "id=" + id +
//                ", description='" + description + '\'' +
//                ", menu=" + menu +
//                '}';
        return description;
    }


    public JFXCheckBox getCheckBox() {
        return checkBox;
    }

    public void setCheckBox(JFXCheckBox checkBox) {
        this.checkBox = checkBox;
    }
}
