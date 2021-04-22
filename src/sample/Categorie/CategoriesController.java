package sample.Categorie;

import Entities.Categorie;
import Entities.Menu;
import Services.CategorieService;
import Services.IngredientService;
import Services.MenuService;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class CategoriesController implements Initializable {

    public TableView tableView;
    public FontAwesomeIconView btnNew;
    public MenuBar eventMenu;
    public JFXTextField searchField;
    private ObservableList<Categorie> tvObservableList;

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
                System.out.println("RESSSSSSSSSS"+res);
                FXMLLoader loader = new FXMLLoader(getClass().getResource(res));
                Parent root = null;
                try {
                    root = loader.load();
                    tableView.getScene().setRoot(root);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        });

        tvObservableList = FXCollections.observableArrayList();


        List<Categorie> list = new CategorieService().findAll();
        tvObservableList.addAll(list);



        TableColumn<Categorie, String> colName = new TableColumn<>("Nom");
        colName.setCellValueFactory(new PropertyValueFactory<>("nom"));

        TableColumn<Categorie, String> colDescription = new TableColumn<>("Description");
        colDescription.setCellValueFactory(new PropertyValueFactory<>("Description"));

        tableView.setItems(tvObservableList);
        tableView.getColumns().addAll(colName, colDescription);




        TableColumn<Categorie, Void> colBtn = new TableColumn<>("Supprimer");

        Callback<TableColumn<Categorie, Void>, TableCell<Categorie, Void>> cellFactory = new Callback<TableColumn<Categorie, Void>, TableCell<Categorie, Void>>() {

            @Override
            public TableCell<Categorie, Void> call(final TableColumn<Categorie, Void> param) {
                final TableCell<Categorie, Void> cell = new TableCell<Categorie, Void>() {

                    private final JFXButton btnDelete = new JFXButton();

                    {
                        FontAwesomeIconView deleteIcon = new FontAwesomeIconView(FontAwesomeIcon.TRASH);
                        deleteIcon.setSize("20px");
                        btnDelete.setGraphic(deleteIcon);
                        btnDelete.setOnAction((ActionEvent event) -> {
                            Categorie ev = getTableView().getItems().get(getIndex());

                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                            alert.setTitle("Confirmation Dialog");
                            alert.setHeaderText("Supprimer " + ev.getNom() + " ?");

                            Optional<ButtonType> result = alert.showAndWait();
                            if (result.get() == ButtonType.OK){
                                int delete = new CategorieService().delete(ev.getId());
                                if (delete == 1) {
                                    System.out.println("Deleted");
                                    tvObservableList.remove(ev);
                                }


                            }

                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btnDelete);
                        }
                    }
                };
                return cell;
            }
        };

        colBtn.setCellFactory(cellFactory);

        tableView.getColumns().add(colBtn);




        TableColumn<Categorie, Void> colBtn2 = new TableColumn<>("Update");

        Callback<TableColumn<Categorie, Void>, TableCell<Categorie, Void>> cellFactory2 = new Callback<TableColumn<Categorie, Void>, TableCell<Categorie, Void>>() {

            @Override
            public TableCell<Categorie, Void> call(final TableColumn<Categorie, Void> param) {
                final TableCell<Categorie, Void> cell = new TableCell<Categorie, Void>() {

                    private final JFXButton btnUpdate = new JFXButton();

                    {
                        FontAwesomeIconView editIcon = new FontAwesomeIconView(FontAwesomeIcon.EDIT);
                        editIcon.setSize("20px");
                        btnUpdate.setGraphic(editIcon);
                        btnUpdate.setOnAction((ActionEvent event) -> {
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("UpdateCategorie.fxml"));
                            Parent root = null;
                            try {
                                root = loader.load();
                                UpdateCategorieController updateController = loader.getController();
                                updateController.setCategorie(getTableView().getItems().get(getIndex()));
                                updateController.tfName.setText(getTableView().getItems().get(getIndex()).getNom());
                                updateController.tfDescription.setText(getTableView().getItems().get(getIndex()).getDescription());
                                tableView.getScene().setRoot(root);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btnUpdate);
                        }
                    }
                };
                return cell;
            }
        };

        colBtn2.setCellFactory(cellFactory2);

        tableView.getColumns().add(colBtn2);


        TableColumn<Categorie, Void> colBtn3 = new TableColumn<>("Menus");

        Callback<TableColumn<Categorie, Void>, TableCell<Categorie, Void>> cellFactory3 = new Callback<TableColumn<Categorie, Void>, TableCell<Categorie, Void>>() {

            @Override
            public TableCell<Categorie, Void> call(final TableColumn<Categorie, Void> param) {
                final TableCell<Categorie, Void> cell = new TableCell<Categorie, Void>() {

                    private final JFXButton btnMenus = new JFXButton();

                    {
                        FontAwesomeIconView showIcon = new FontAwesomeIconView(FontAwesomeIcon.EYE);
                        showIcon.setSize("20px");
                        btnMenus.setGraphic(showIcon);
                        btnMenus.setOnAction((ActionEvent event) -> {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Menus");
                            alert.setHeaderText(null);
                            alert.setContentText(new MenuService().findByCategorie(getTableView().getItems().get(getIndex())).toString());
                            alert.showAndWait();
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btnMenus);
                        }
                    }
                };
                return cell;
            }
        };

        colBtn3.setCellFactory(cellFactory3);
        tableView.getColumns().add(colBtn3);


    }

    public void newCategorie(MouseEvent mouseEvent) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AddCategorie.fxml"));
        Parent root = null;
        try {
            root = loader.load();
            tableView.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void search(KeyEvent keyEvent) {
        if (searchField.getText().equals(""))
            tableView.setItems(tvObservableList);
        else {
            FilteredList<Categorie> x = tvObservableList.filtered(item -> item.getNom().toLowerCase().contains(searchField.getText().toLowerCase()));
            tableView.setItems(x);
        }
    }
}
