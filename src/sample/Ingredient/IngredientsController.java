package sample.Ingredient;

import Entities.Ingredient;
import Services.CategorieService;
import Services.IngredientService;
import Services.MenuService;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
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
import javafx.scene.control.MenuBar;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import javafx.util.Duration;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
    public JFXButton export;
    private ObservableList<Ingredient> tvObservableList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if(new IngredientService().findAll().size() == 0)
            export.setVisible(false);

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
                                    System.out.println(tvObservableList.remove(ev));
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

    public void export(ActionEvent actionEvent) throws DocumentException, FileNotFoundException {

        /* Step-2: Initialize PDF documents - logical objects */
        Document my_pdf_report = new Document();

        String home = System.getProperty("user.home");
        PdfWriter.getInstance(my_pdf_report, new FileOutputStream(home+"/Downloads/Ingrédients_PDF.pdf"));

        my_pdf_report.open();
        PdfPTable my_report_table = new PdfPTable(2);
        //create a cell object

        PdfPCell descriptionCell = new PdfPCell(new Phrase("Description"));
        descriptionCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        descriptionCell.setBackgroundColor(BaseColor.CYAN);
        my_report_table.addCell(descriptionCell);

        PdfPCell Menus = new PdfPCell(new Phrase("Menu"));
        Menus.setHorizontalAlignment(Element.ALIGN_CENTER);
        Menus.setBackgroundColor(BaseColor.CYAN);
        my_report_table.addCell(Menus);

        new IngredientService().findAll().forEach(ingredient -> {
            String description = ingredient.getDescription();
            String menu = ingredient.getMenu().getName() ;
            my_report_table.addCell(new PdfPCell(new Phrase(description)));
            my_report_table.addCell(new PdfPCell(new Phrase(menu)));
        });

        /* Attach report table to PDF */
        my_pdf_report.add(my_report_table);
        my_pdf_report.close();
        new TrayNotification("PDF téléchargé !", " PDF des Ingredient sous /Téléchargement ", NotificationType.SUCCESS).showAndDismiss(Duration.seconds(5));

        File file = new File(home+"/Downloads/Ingrédients_PDF.pdf");
        try {
            Desktop.getDesktop().open(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
