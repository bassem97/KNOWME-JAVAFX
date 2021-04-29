package sample.Menu;

import Entities.Categorie;
import Entities.Menu;
import Services.CategorieService;
import Services.MenuService;
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
import javafx.util.Duration;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;


import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.ResourceBundle;

public class AddMenuController implements Initializable {

    public JFXTextField tfName;
    public JFXTextField tfDescription;
    public JFXTextField tfNbMax;
    public JFXDatePicker datePicker;
    public JFXButton btnAjouter;
    public JFXListView listViewType;
    public Button imageButton;
    public ImageView imageView;
    private String imageName = null;
    private File imageFile;
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
                    tfName.getScene().setRoot(root);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        });



        ObservableList<Categorie> items = FXCollections.observableArrayList();
        items.addAll(new CategorieService().findAll());
        listViewType.setItems(items);
    }

    public void saveMenu(ActionEvent actionEvent) {
        Menu menu = new Menu();
        menu.setCategorie(new CategorieService().findById(((Categorie)listViewType.getSelectionModel().getSelectedItem()).getId()));
        menu.setName(tfName.getText());
        menu.setDescription(tfDescription.getText());
        // Image
        if (imageName != null) {
            menu.setImg(imageName);
            try {

                ImageInputStream iis = ImageIO.createImageInputStream(imageFile);

                Iterator<ImageReader> imageReaders = ImageIO.getImageReaders(iis);

                while (imageReaders.hasNext()) {

                    ImageReader reader = imageReaders.next();
                    ImageIO.write(ImageIO.read(imageFile), reader.getFormatName(), new File("src/assets/" + imageFile.getName()).getAbsoluteFile());
                }

            }
            catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }

        new MenuService().save(menu);

        new TrayNotification("Information", menu.getName() + " a été Crée avec succès !", NotificationType.SUCCESS).showAndDismiss(Duration.seconds(5));

        FXMLLoader loader = new FXMLLoader(getClass().getResource("menus.fxml"));
        Parent root = null;
        try {
            root = loader.load();
            btnAjouter.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void uploadImage(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Images", "*.*"));
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            imageView.setImage(new Image(selectedFile.toURI().toString()));
            imageFile = selectedFile;
            imageName = selectedFile.getName();
        }
    }
}
