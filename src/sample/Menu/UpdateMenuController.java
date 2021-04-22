package sample.Menu;

import Entities.Categorie;
import Entities.Ingredient;
import Entities.Menu;
import Services.CategorieService;
import Services.IngredientService;
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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.ResourceBundle;

public class UpdateMenuController implements Initializable {

    public JFXTextField tfName;
    public JFXTextField tfDescription;
    public JFXTextField tfNbMax;
    public JFXDatePicker datePicker = new JFXDatePicker();
    public JFXButton btnModifier;
    public JFXListView listViewType;
    public Button imageButton;
    public ImageView imageView;
    private String imageName = null;
    private File imageFile;
    private Menu menu;
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

    public void updateMenu(ActionEvent actionEvent) {
        menu.setName(tfName.getText());
        menu.setDescription(tfDescription.getText());
        menu.setCategorie(new CategorieService().findById(((Categorie)listViewType.getSelectionModel().getSelectedItem()).getId()));

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
    }

    public JFXTextField getTfName() {
        return tfName;
    }

    public void setTfName(JFXTextField tfName) {
        this.tfName = tfName;
    }

    public JFXTextField getTfDescription() {
        return tfDescription;
    }

    public void setTfDescription(JFXTextField tfDescription) {
        this.tfDescription = tfDescription;
    }

    public JFXTextField getTfNbMax() {
        return tfNbMax;
    }

    public void setTfNbMax(JFXTextField tfNbMax) {
        this.tfNbMax = tfNbMax;
    }

    public JFXDatePicker getDatePicker() {
        return datePicker;
    }

    public void setDatePicker(JFXDatePicker datePicker) {
        this.datePicker = datePicker;
    }

    public JFXButton getBtnModifier() {
        return btnModifier;
    }

    public void setBtnModifier(JFXButton btnModifier) {
        this.btnModifier = btnModifier;
    }

    public JFXListView getListViewType() {
        return listViewType;
    }

    public void setListViewType(JFXListView listViewType) {
        this.listViewType = listViewType;
    }

    public Button getImageButton() {
        return imageButton;
    }

    public void setImageButton(Button imageButton) {
        this.imageButton = imageButton;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public File getImageFile() {
        return imageFile;
    }

    public void setImageFile(File imageFile) {
        this.imageFile = imageFile;
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }
}
