package sample.Menu;

import Entities.Ingredient;
import Entities.Menu;
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

public class MenusController implements Initializable {

    public TableView tableView;
    public FontAwesomeIconView btnNew;
    public MenuBar eventMenu;
    public JFXTextField searchField;
    private ObservableList<Menu> tvObservableList;

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
                    tableView.getScene().setRoot(root);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        });

        tvObservableList = FXCollections.observableArrayList();


        /** Initialize image **/
        List<Menu> list = new MenuService().findAll();
        list.forEach(Menu::initializeImageView);

        tvObservableList.addAll(list);


        TableColumn<Menu, ImageView> colImg = new TableColumn<>("Image");
        colImg.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getImageView()));


        TableColumn<Menu, String> colName = new TableColumn<>("Nom");
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Menu, String> colCateg = new TableColumn<>("Categorie");
        colCateg.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getCategorie().getNom()));

        TableColumn<Menu, String> colExpDate = new TableColumn<>("Expiration date");
        String pattern = "dd/MM/yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        colExpDate.setCellValueFactory(cellData -> new SimpleObjectProperty<>(simpleDateFormat.format(Date.valueOf(cellData.getValue().getExpiration_date().toLocalDate()))));

        tableView.setItems(tvObservableList);
        tableView.getColumns().addAll(colImg, colName, colCateg, colExpDate);




        TableColumn<Menu, Void> colBtn = new TableColumn<>("Supprimer");

        Callback<TableColumn<Menu, Void>, TableCell<Menu, Void>> cellFactory = new Callback<TableColumn<Menu, Void>, TableCell<Menu, Void>>() {

            @Override
            public TableCell<Menu, Void> call(final TableColumn<Menu, Void> param) {
                final TableCell<Menu, Void> cell = new TableCell<Menu, Void>() {

                    private final JFXButton btnDelete = new JFXButton();

                    {
                        FontAwesomeIconView deleteIcon = new FontAwesomeIconView(FontAwesomeIcon.TRASH);
                        deleteIcon.setSize("20px");
                        btnDelete.setGraphic(deleteIcon);
                        btnDelete.setOnAction((ActionEvent event) -> {
                            Menu ev = getTableView().getItems().get(getIndex());

                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                            alert.setTitle("Confirmation Dialog");
                            alert.setHeaderText("Supprimer " + ev.getName() + " ?");

                            Optional<ButtonType> result = alert.showAndWait();
                            if (result.get() == ButtonType.OK){
                                int delete = new MenuService().delete(ev.getId());
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




        TableColumn<Menu, Void> colBtn2 = new TableColumn<>("Update");

        Callback<TableColumn<Menu, Void>, TableCell<Menu, Void>> cellFactory2 = new Callback<TableColumn<Menu, Void>, TableCell<Menu, Void>>() {

            @Override
            public TableCell<Menu, Void> call(final TableColumn<Menu, Void> param) {
                final TableCell<Menu, Void> cell = new TableCell<Menu, Void>() {

                    private final JFXButton btnUpdate = new JFXButton();

                    {
                        FontAwesomeIconView editIcon = new FontAwesomeIconView(FontAwesomeIcon.EDIT);
                        editIcon.setSize("20px");
                        btnUpdate.setGraphic(editIcon);
                        btnUpdate.setOnAction((ActionEvent event) -> {
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("UpdateMenu.fxml"));
                            Parent root = null;
                            try {
                                root = loader.load();
                                UpdateMenuController updateController = loader.getController();
                                updateController.setMenu(getTableView().getItems().get(getIndex()));
                                updateController.getTfName().setText(getTableView().getItems().get(getIndex()).getName());
                                updateController.getTfDescription().setText(getTableView().getItems().get(getIndex()).getDescription());
                                updateController.setImageView(getTableView().getItems().get(getIndex()).getImageView());
                                updateController.getListViewType().getSelectionModel().select(getTableView().getItems().get(getIndex()).getCategorie());

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



        TableColumn<Menu, Void> colBtn3 = new TableColumn<>("Ingredients");

        Callback<TableColumn<Menu, Void>, TableCell<Menu, Void>> cellFactory3 = new Callback<TableColumn<Menu, Void>, TableCell<Menu, Void>>() {

            @Override
            public TableCell<Menu, Void> call(final TableColumn<Menu, Void> param) {
                final TableCell<Menu, Void> cell = new TableCell<Menu, Void>() {

                    private final JFXButton btnSponsors = new JFXButton();

                    {
                        FontAwesomeIconView showIcon = new FontAwesomeIconView(FontAwesomeIcon.EYE);
                        showIcon.setSize("20px");
                        btnSponsors.setGraphic(showIcon);
                        btnSponsors.setOnAction((ActionEvent event) -> {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Ingredients");
                            alert.setHeaderText(null);
                            alert.setContentText(new IngredientService().findByMenu(getTableView().getItems().get(getIndex())).toString());
                            alert.showAndWait();
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btnSponsors);
                        }
                    }
                };
                return cell;
            }
        };

        colBtn3.setCellFactory(cellFactory3);
        tableView.getColumns().add(colBtn3);


    }

    public void newMenu(MouseEvent mouseEvent) {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("AddMenu.fxml"));
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
            FilteredList<Menu> x = tvObservableList.filtered(item -> item.getName().toLowerCase().contains(searchField.getText().toLowerCase()));
            tableView.setItems(x);
        }
    }
}
