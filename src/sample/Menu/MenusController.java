package sample.Menu;

import Entities.Ingredient;
import Entities.Menu;
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
import com.sun.javafx.application.HostServicesDelegate;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.application.HostServices;
import javafx.application.HostServices.*;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
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
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

import com.itextpdf.text.Image;



public class MenusController implements Initializable {

    public TableView tableView;
    public FontAwesomeIconView btnNew;
    public MenuBar eventMenu;
    public JFXTextField searchField;
    public JFXButton export;
    private ObservableList<Menu> tvObservableList;
    private HostServices hostServices ;

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

    public void export(ActionEvent actionEvent) throws FileNotFoundException, DocumentException {
        Document my_pdf_report = new Document();

        String home = System.getProperty("user.home");
        PdfWriter.getInstance(my_pdf_report, new FileOutputStream(home+"/Downloads/Menus_PDF.pdf"));
        my_pdf_report.open();
        //we have four columns in our table
        PdfPTable my_report_table = new PdfPTable(5);
        //create a cell object
        PdfPCell nom = new PdfPCell(new Phrase("Image"));
        nom.setHorizontalAlignment(Element.ALIGN_CENTER);
        nom.setBackgroundColor(BaseColor.CYAN);
        my_report_table.addCell(nom);

        PdfPCell descriptionCell = new PdfPCell(new Phrase("Nom"));
        descriptionCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        descriptionCell.setBackgroundColor(BaseColor.CYAN);
        my_report_table.addCell(descriptionCell);

        PdfPCell Menus = new PdfPCell(new Phrase("Categorie"));
        Menus.setHorizontalAlignment(Element.ALIGN_CENTER);
        Menus.setBackgroundColor(BaseColor.CYAN);
        my_report_table.addCell(Menus);

        PdfPCell expDate = new PdfPCell(new Phrase("Date expiration"));
        expDate.setHorizontalAlignment(Element.ALIGN_CENTER);
        expDate.setBackgroundColor(BaseColor.CYAN);
        my_report_table.addCell(expDate);

        PdfPCell ingredientsCell = new PdfPCell(new Phrase("Ingredients"));
        ingredientsCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        ingredientsCell.setBackgroundColor(BaseColor.CYAN);
        my_report_table.addCell(ingredientsCell);

        List<Menu> list = new MenuService().findAll();
        list.forEach(Menu::initializeImageView);
        list.forEach(menu -> {
            System.out.println(menu);
            String name = menu.getName();
            String categorie = menu.getCategorie().getNom();
            LocalDateTime expiration_date = menu.getExpiration_date();
            String ingredient = new IngredientService().findByMenu(new MenuService().findById(menu.getId())).toString();


            PdfPCell image = null;
            try {
                image = new PdfPCell(Image.getInstance("D:/Desktop/knowme/src/assets/"+menu.getImg()) , true);
            } catch (BadElementException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            my_report_table.addCell(image);
            my_report_table.addCell(new PdfPCell(new Phrase(name)));
            my_report_table.addCell(new PdfPCell(new Phrase(categorie)));
            my_report_table.addCell(new PdfPCell(new Phrase(expiration_date.toString())));
            my_report_table.addCell(new PdfPCell(new Phrase(ingredient)));
        });

        /* Attach report table to PDF */
        my_pdf_report.add(my_report_table);
        my_pdf_report.close();


        // Open file from /downloads
        File file = new File(home+"/Downloads/Menus_PDF.pdf");
        try {
            Desktop.getDesktop().open(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        new TrayNotification("PDF téléchargé !", " vérifiez votre dossier de téléchargement ", NotificationType.SUCCESS).showAndDismiss(Duration.seconds(5));
    }

    public void exportExcel(ActionEvent actionEvent) throws IOException {

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Reviews");

        String home = System.getProperty("user.home");
        String excelFilePath = home+"/Downloads/Menus-Excel.xlsx";

        writeHeaderLine(sheet);

        List<Menu> list = new MenuService().findAll();
        writeDataLines( list,workbook, sheet);

        FileOutputStream outputStream = new FileOutputStream(excelFilePath);
        workbook.write(outputStream);

        new TrayNotification("Excel téléchargé !", " vérifiez votre dossier de téléchargement ", NotificationType.SUCCESS).showAndDismiss(Duration.seconds(5));


        File file = new File(excelFilePath);
        try {
            Desktop.getDesktop().open(file);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void writeDataLines(List<Menu> list, XSSFWorkbook workbook, XSSFSheet sheet) {

        AtomicInteger rowCount = new AtomicInteger(1);

        list.forEach(menu ->  {
            String nom = menu.getName();
            String description = menu.getDescription();
            String expiration = menu.getExpiration_date().toString();
            String categorie = menu.getCategorie().getNom();

            Row row = sheet.createRow(rowCount.getAndIncrement());

            int columnCount = 0;
            org.apache.poi.ss.usermodel.Cell cell = row.createCell(columnCount++);
            cell.setCellValue(nom);

            cell = row.createCell(columnCount++);
            cell.setCellValue(description);

            cell = row.createCell(columnCount++);
            cell.setCellValue(expiration);

            cell = row.createCell(columnCount++);
            cell.setCellValue(categorie);
        });

    }

    private void writeHeaderLine(XSSFSheet sheet) {
        Row headerRow = sheet.createRow(0);

        Cell headerCell = headerRow.createCell(0);
        headerCell.setCellValue("NOM");

        headerCell = headerRow.createCell(1);
        headerCell.setCellValue("DESCRIPTION");

        headerCell = headerRow.createCell(2);
        headerCell.setCellValue("DATE EXPIRATION");

        headerCell = headerRow.createCell(3);
        headerCell.setCellValue("CATEGORIE");



    }


}
