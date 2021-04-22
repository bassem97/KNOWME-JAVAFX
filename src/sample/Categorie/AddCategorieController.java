package sample.Categorie;

import Entities.Categorie;
import Services.CategorieService;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.ResourceBundle;

public class AddCategorieController implements Initializable {

    public JFXTextField tfName;
    public JFXTextField tfDescription;
    public JFXButton btnAjouter;
    public MenuBar eventMenu;

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
                        res = "../Categorie/Categories.fxml";
                        break;

                    default:
                        res = "../Categorie/Categories.fxml";
                        break;
                }

                FXMLLoader loader = new FXMLLoader(getClass().getResource(res));
                Parent root = null;
                try {
                    root = loader.load();
                    btnAjouter.getScene().setRoot(root);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        });


//        ObservableList<Categorie> items = FXCollections.observableArrayList();
//        items.addAll(new CategorieService().findAll());
//        listViewType.setItems(items);
    }

    public void saveCategorie(ActionEvent actionEvent) {
        Categorie categorie = new Categorie();
        categorie.setNom(tfName.getText());
        categorie.setDescription(tfDescription.getText());
        new CategorieService().save(categorie);
        new tools.Alerts.Alert(Alert.AlertType.INFORMATION,"Categorie ajouté  !",null,categorie.getNom()+" est ajouté !");

        FXMLLoader loader = new FXMLLoader(getClass().getResource("Categories.fxml"));
        Parent root = null;
        try {
            root = loader.load();
            btnAjouter.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}

