package sample.Ingredient;

import Entities.Categorie;
import Entities.Ingredient;
import Entities.Menu;
import Services.CategorieService;
import Services.IngredientService;
import Services.MenuService;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.MenuBar;
import javafx.util.Duration;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class UpdateIngredientController implements Initializable {

    public JFXTextField tfDescription;
    public MenuBar eventMenu;
    public JFXButton btnModifier;
    private  Ingredient ingredient;
    public JFXListView listViewMenu;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // Menu Onclick
        eventMenu.getMenus().get(0).getItems().forEach(menuItem -> {
            menuItem.setOnAction(event -> {
                String res;
                switch (menuItem.getText()) {
                    case "Liste Des Ingredient":
                        res = "../Ingredient/ingredients.fxml";
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
                    tfDescription.getScene().setRoot(root);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        });

        ObservableList<Menu> items = FXCollections.observableArrayList();
        items.addAll(new MenuService().findAll());
        listViewMenu.setItems(items);

    }

    public void updateIngredient(ActionEvent actionEvent) {
        ingredient.setDescription(tfDescription.getText());
        ingredient.setMenu(new MenuService().findById(((Menu)listViewMenu.getSelectionModel().getSelectedItem()).getId()));
        new IngredientService().update(ingredient);
        new TrayNotification("Information", ingredient.getDescription() + " a ??t?? Modifi?? avec succ??s !", NotificationType.SUCCESS).showAndDismiss(Duration.seconds(5));

        FXMLLoader loader = new FXMLLoader(getClass().getResource("ingredients.fxml"));
        Parent root = null;
        try {
            root = loader.load();
            tfDescription.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public JFXTextField getTfDescription() {
        return tfDescription;
    }

    public void setTfDescription(JFXTextField tfDescription) {
        this.tfDescription = tfDescription;
    }

    public JFXListView getListViewMenu() {
        return listViewMenu;
    }

    public void setListViewMenu(JFXListView listViewMenu) {
        this.listViewMenu = listViewMenu;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public void setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
    }
}

