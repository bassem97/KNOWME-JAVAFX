package sample.Categorie;

import Entities.Categorie;
import Services.CategorieService;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuBar;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class UpdateCategorieController implements Initializable {

    public JFXTextField tfName;
    public JFXTextField tfDescription;
    public MenuBar eventMenu;
    public JFXButton btnModifier;
    private Categorie categorie;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // Menu Onclick
        eventMenu.getMenus().get(0).getItems().forEach(menuItem -> {
            menuItem.setOnAction(event -> {
                String res;
                switch (menuItem.getText()) {
                    case "Liste Des Ingredient":
                        res = "../Ingredient/menus.fxml";
                        break;

                    case "Listes des menus":
                        res = "../Menu/menus.fxml";
                        break;

                    case "Les categories":
                        res = "categories.fxml";
                        break;

                    default:
                        res = "menus.fxml";
                        break;
                }

                FXMLLoader loader = new FXMLLoader(getClass().getResource(res));
                Parent root = null;
                try {
                    root = loader.load();
                    btnModifier.getScene().setRoot(root);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        });

    }

    public void updateCategorie(ActionEvent actionEvent) {
        categorie.setNom(tfName.getText());
        categorie.setDescription(tfDescription.getText());
        new CategorieService().update(categorie);
        new tools.Alerts.Alert(Alert.AlertType.INFORMATION,"Categorie modifié  !",null,categorie.getNom()+" est modifié !");

        FXMLLoader loader = new FXMLLoader(getClass().getResource("Categories.fxml"));
        Parent root = null;
        try {
            root = loader.load();
            btnModifier.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public Categorie getCategorie() {
        return categorie;
    }

    public void setCategorie(Categorie categorie) {
        this.categorie = categorie;
    }
}

