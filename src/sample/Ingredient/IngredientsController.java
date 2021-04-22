package sample.Ingredient;

import Entities.Ingredient;
import Services.IngredientService;
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

public class IngredientsController implements Initializable {

    public TableView tableView;
    public FontAwesomeIconView btnNew;
    public MenuBar eventMenu;
    public JFXTextField searchField;
    private ObservableList<Ingredient> tvObservableList;

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
                    tableView.getScene().setRoot(root);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        });

        tvObservableList = FXCollections.observableArrayList();


        List<Ingredient> list = new IngredientService().findAll();
        tvObservableList.addAll(list);


        TableColumn<Ingredient, ImageView> colDescription = new TableColumn<>("description");
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));


        TableColumn<Ingredient, String> colMenuId = new TableColumn<>("menu");
        colMenuId.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getMenu().getName()));

//        TableColumn<Ingredient, String> colDate = new TableColumn<>("Date");
//        String pattern = "dd/MM/yyyy";
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
//        colDate.setCellValueFactory(cellData -> new SimpleObjectProperty<>(simpleDateFormat.format(Date.valueOf(cellData.getValue().getDate().toLocalDate()))));

        tableView.setItems(tvObservableList);
        tableView.getColumns().addAll(colDescription, colMenuId);




        TableColumn<Ingredient, Void> colBtn = new TableColumn<>("Supprimer");

        Callback<TableColumn<Ingredient, Void>, TableCell<Ingredient, Void>> cellFactory = new Callback<TableColumn<Ingredient, Void>, TableCell<Ingredient, Void>>() {

            @Override
            public TableCell<Ingredient, Void> call(final TableColumn<Ingredient, Void> param) {
                final TableCell<Ingredient, Void> cell = new TableCell<Ingredient, Void>() {

                    private final JFXButton btnDelete = new JFXButton();

                    {
                        FontAwesomeIconView deleteIcon = new FontAwesomeIconView(FontAwesomeIcon.TRASH);
                        deleteIcon.setSize("20px");
                        btnDelete.setGraphic(deleteIcon);
                        btnDelete.setOnAction((ActionEvent event) -> {
                            Ingredient ev = getTableView().getItems().get(getIndex());

                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                            alert.setTitle("Confirmation Dialog");
                            alert.setHeaderText("Supprimer " + ev.getDescription() + " ?");

                            Optional<ButtonType> result = alert.showAndWait();
                            if (result.get() == ButtonType.OK){
                                int delete = new IngredientService().delete(ev.getId());
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




        TableColumn<Ingredient, Void> colBtn2 = new TableColumn<>("Update");

        Callback<TableColumn<Ingredient, Void>, TableCell<Ingredient, Void>> cellFactory2 = new Callback<TableColumn<Ingredient, Void>, TableCell<Ingredient, Void>>() {

            @Override
            public TableCell<Ingredient, Void> call(final TableColumn<Ingredient, Void> param) {
                final TableCell<Ingredient, Void> cell = new TableCell<Ingredient, Void>() {

                    private final JFXButton btnUpdate = new JFXButton();

                    {
                        FontAwesomeIconView editIcon = new FontAwesomeIconView(FontAwesomeIcon.EDIT);
                        editIcon.setSize("20px");
                        btnUpdate.setGraphic(editIcon);
                        btnUpdate.setOnAction((ActionEvent event) -> {
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("UpdateIngredient.fxml"));
                            Parent root = null;
                            try {
                                root = loader.load();
                                UpdateIngredientController updateController = loader.getController();
                                updateController.setIngredient(getTableView().getItems().get(getIndex()));
                                updateController.getTfDescription().setText(getTableView().getItems().get(getIndex()).getDescription());
                                updateController.getListViewMenu().getSelectionModel().select(getTableView().getItems().get(getIndex()).getMenu());

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



//        TableColumn<Ingredient, Void> colBtn3 = new TableColumn<>("Sponsors");
//
//        Callback<TableColumn<Ingredient, Void>, TableCell<Ingredient, Void>> cellFactory3 = new Callback<TableColumn<Ingredient, Void>, TableCell<Ingredient, Void>>() {
//
//            @Override
//            public TableCell<Ingredient, Void> call(final TableColumn<Ingredient, Void> param) {
//                final TableCell<Ingredient, Void> cell = new TableCell<Ingredient, Void>() {
//
//                    private final JFXButton btnSponsors = new JFXButton();
//
//                    {
//                        FontAwesomeIconView showIcon = new FontAwesomeIconView(FontAwesomeIcon.EYE);
//                        showIcon.setSize("20px");
//                        btnSponsors.setGraphic(showIcon);
//                        btnSponsors.setOnAction((ActionEvent event) -> {
//                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
//                            alert.setTitle("Sponsors");
//                            alert.setHeaderText(null);
//                            alert.setContentText(new EventSponsorService().findByEvent(getTableView().getItems().get(getIndex())).toString());
//                            alert.showAndWait();
//                        });
//                    }
//
//                    @Override
//                    public void updateItem(Void item, boolean empty) {
//                        super.updateItem(item, empty);
//                        if (empty) {
//                            setGraphic(null);
//                        } else {
//                            setGraphic(btnSponsors);
//                        }
//                    }
//                };
//                return cell;
//            }
//        };
//
//        colBtn3.setCellFactory(cellFactory3);
//        tableView.getColumns().add(colBtn3);

//        TableColumn<Ingredient, Void> colBtn4 = new TableColumn<>("Participer");
//
//        Callback<TableColumn<Ingredient, Void>, TableCell<Ingredient, Void>> cellFactory4 = new Callback<TableColumn<Ingredient, Void>, TableCell<Ingredient, Void>>() {
//
//            @Override
//            public TableCell<Ingredient, Void> call(final TableColumn<Ingredient, Void> param) {
//                final TableCell<Ingredient, Void> cell = new TableCell<Ingredient, Void>() {
//
//                    private final JFXButton btnParticiper = new JFXButton();
//
//                    {
//                        FontAwesomeIconView showIcon = new FontAwesomeIconView(FontAwesomeIcon.CHEVRON_CIRCLE_RIGHT);
//                        showIcon.setSize("20px");
//                        btnParticiper.setGraphic(showIcon);
//                        btnParticiper.setOnAction((ActionEvent event) -> {
//
//
//                            if (getTableView().getItems().get(getIndex()).getDate().toLocalDate().isBefore(LocalDate.now())) {
//                                Alert alert = new Alert(Alert.AlertType.INFORMATION);
//                                alert.setTitle("Information Dialog");
//                                alert.setHeaderText(null);
//                                alert.setContentText("Evènement déjà passé");
//                                alert.showAndWait();
//                            } else if (new ParticipantService().findByEventId(getTableView().getItems().get(getIndex()).getId()).size() >= getTableView().getItems().get(getIndex()).getNb_part_max()) {
//                                Alert alert = new Alert(Alert.AlertType.INFORMATION);
//                                alert.setTitle("Information Dialog");
//                                alert.setHeaderText(null);
//                                alert.setContentText("Pas de place disponible");
//                                alert.showAndWait();
//                            } else if (new ParticipantService().findByUserIdAndEventId(UserSession.getUser().getId(), getTableView().getItems().get(getIndex()).getId()) == null) {
//                                Participant participant = new Participant(getTableView().getItems().get(getIndex()), UserSession.getUser());
//                                new ParticipantService().save(participant);
//                                Alert alert = new Alert(Alert.AlertType.INFORMATION);
//                                alert.setTitle("Information Dialog");
//                                alert.setHeaderText(null);
//                                alert.setContentText("Vous participer à l'event " + getTableView().getItems().get(getIndex()).getName());
//                                alert.showAndWait();
//                                FXMLLoader loader = new FXMLLoader(getClass().getResource("../Participant/ParticipantList.fxml"));
//                                Parent root = null;
//                                try {
//                                    root = loader.load();
//                                    tableView.getScene().setRoot(root);
//                                } catch (IOException e) {
//                                    e.printStackTrace();
//                                }
//                            } else if (new ParticipantService().findByUserIdAndEventId(UserSession.getUser().getId(), getTableView().getItems().get(getIndex()).getId()) != null) {
//                                Alert alert = new Alert(Alert.AlertType.INFORMATION);
//                                alert.setTitle("Information Dialog");
//                                alert.setHeaderText(null);
//                                alert.setContentText("Vous participez déjà à cet Ingredient");
//                                alert.showAndWait();
//                            }
//                        });
//                    }
//
//                    @Override
//                    public void updateItem(Void item, boolean empty) {
//                        super.updateItem(item, empty);
//                        if (empty) {
//                            setGraphic(null);
//                        } else {
//                            setGraphic(btnParticiper);
//                        }
//                    }
//                };
//                return cell;
//            }
//        };
//
//        colBtn4.setCellFactory(cellFactory4);
//        tableView.getColumns().add(colBtn4);



    }

    public void newIngredient(MouseEvent mouseEvent) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AddIngredient.fxml"));
        Parent root = null;
        try {
            root = loader.load();
            tableView.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showStats(MouseEvent mouseEvent) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("EventStats.fxml"));
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
            FilteredList<Ingredient> x = tvObservableList.filtered(item -> item.getDescription().toLowerCase().contains(searchField.getText().toLowerCase()));
            tableView.setItems(x);
        }
    }
}
