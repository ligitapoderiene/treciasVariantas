package com.example.knygos2.controler;

import com.example.knygos2.MainApplication;
import com.example.knygos2.model.Book;
import com.example.knygos2.model.BookDao;
import com.example.knygos2.model.UserDao;
//import com.example.knygos2.model.UserSingleton;
import com.example.knygos2.utils.Validation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {
    @FXML
    private Label status;
    @FXML
    private TableView bookTableView;
    @FXML
    private TextField idField;

    // Lentelės stulpeliai
    @FXML
    private TableColumn idColumn;
    @FXML
    private TableColumn titleColumn;
    @FXML
    private TableColumn pageColumn;
    @FXML
    private TableColumn categoryColumn;
    @FXML
    private TableColumn autorColumn;
    @FXML
    private TableColumn summaryColumn;

    // Formos elementai
    @FXML
    private TextField titleField;
    @FXML
    private TextField pageField;

    //
    @FXML
    private CheckBox checkBoxGrozineLiteratura;
    @FXML
    private CheckBox checkBoxMokslineLiteratura;
    @FXML
    private CheckBox checkBoxPoezija;

    //
    @FXML
    private RadioButton radioButtonAntanas;
    @FXML
    private RadioButton radioButtonJonas;
    @FXML
    private RadioButton radioButtonStyvas;
    //
    @FXML
    private ToggleGroup radioGroup;
    //
    @FXML
    private ChoiceBox choiceBoxSummary;
    //Buttons
    @FXML
    private Button createButton, searchButton, updateButton, deleteButton;


    ObservableList<Book> list = FXCollections.observableArrayList();

    @FXML
    public void searchButtonClick() {
        list.clear();
        String titleField2 = titleField.getText();

        List<Book> bookList = BookDao.searchByName(titleField2);


        for (Book book : bookList) {
            list.add(new Book(book.getId(), book.getTitle(), book.getPage(), book.getCategory(), book.getAutor(), book.getSummary()));

            idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
            titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
            pageColumn.setCellValueFactory(new PropertyValueFactory<>("page"));
            categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
            autorColumn.setCellValueFactory(new PropertyValueFactory<>("autor"));
            summaryColumn.setCellValueFactory(new PropertyValueFactory<>("summary"));

            bookTableView.setItems(list);
        }
        if(bookList.isEmpty()){
            status.setText("Nepavyko atlikti paieška pagal knygos pavadinima");
        } else {
            status.setText("Pavyko atlikti paieška pagal knygos pavadinima");
        }
    }

    @FXML
    public void onCreateButtonClick() {
        String titleField2 = titleField.getText();
        String pageField2 = pageField.getText();          // Paliekam String tipa kad veiktu validacija

        String category = "";
        if (checkBoxGrozineLiteratura.isSelected()) {
            category += checkBoxGrozineLiteratura.getText() + ",";
        }
        if (checkBoxMokslineLiteratura.isSelected()) {
            category += checkBoxMokslineLiteratura.getText() + ",";
        }
        if (checkBoxPoezija.isSelected()) {
            category += checkBoxPoezija.getText() + ",";
        }

        String autor = "";
        if (radioButtonAntanas.isSelected()) {
            autor = radioButtonAntanas.getText();
        } else if (radioButtonJonas.isSelected()) {
            autor = radioButtonJonas.getText();
        } else if (radioButtonStyvas.isSelected()) {
            autor = radioButtonStyvas.getText();
        }

        String summary = "";
        if (!choiceBoxSummary.getSelectionModel().isEmpty()) {
            summary = choiceBoxSummary.getSelectionModel().getSelectedItem().toString();
        }

        // Tikriname pagal Validacijas
        if (!Validation.isValidTitle(titleField2)) {
            status.setText("Neteisingai įvedėte pavadinimą");
        } else if (!Validation.isValidPage(pageField2)) {
            status.setText("Neteisingai įvedėte lapu skaiciu");
        } else if (category.isEmpty()) {
            status.setText("Prašome pasirinkti kategorija");
        } else {
            // keiciame kintamuju tipus pagal konstruktoriu
            int summary2 = Integer.parseInt(summary);
            int pageField3 = Integer.parseInt(pageField.getText());

            // Kuriame įrašą DB
            Book book = new Book(titleField2, pageField3, category, autor, summary);
            BookDao.create(book);
            status.setText("Pavyko sukurti įrašą");
        }
    }

    @FXML
    public void onUpdateButtonClick() {
        String titleField2 = titleField.getText();
        String pageField2 = pageField.getText();          // Paliekam String tipa kad veiktu validacija
        String idField2 = idField.getText();

        String category  = "";
        if (checkBoxGrozineLiteratura.isSelected()) {
            category += checkBoxGrozineLiteratura.getText() + ",";
        }
        if (checkBoxMokslineLiteratura.isSelected()) {
            category+= checkBoxMokslineLiteratura.getText() + ",";
        }
        if (checkBoxPoezija.isSelected()) {
            category+= checkBoxPoezija.getText() + ",";
        }

        String autor = "";
        if (radioButtonJonas.isSelected()) {
            autor = radioButtonJonas.getText();
        } else if (radioButtonAntanas.isSelected()) {
            autor = radioButtonAntanas.getText();
        } else if (radioButtonStyvas.isSelected()) {
            autor = radioButtonStyvas.getText();
        }

        String summary = "";
        if (!choiceBoxSummary.getSelectionModel().isEmpty()) {
            summary = choiceBoxSummary.getSelectionModel().getSelectedItem().toString();
        }

        // Tikriname pagal Validacijas
        if (!Validation.isValidTime(idField2)) {
            status.setText("Neteisingai įvestas ID");
        } else if (!Validation.isValidTitle(titleField2)) {
            status.setText("Neteisingai įvedėte pavadinimą");
        } else if (!Validation.isValidPage(pageField2)) {
            status.setText("Neteisingai įvedėte lapu skaiciu");
        } else if (category.isEmpty()) {
            status.setText("Prašome pasirinkti kategorija");
        } else {
           //* keiciame kintamuju tipus pagal konstruktoriu
            int summary3 = Integer.parseInt(summary);
            int pageField4 = Integer.parseInt(pageField.getText());
            int idField3 = Integer.parseInt(idField.getText());

            // Kuriame įrašą DB
           Book book = new Book(idField2, titleField2, pageField4, category, autor, summary);
            BookDao.update(book);
            status.setText("Pavyko paredaguoti įrašą");
        }
    }

    @FXML
    public void onDeleteButtonClick() {
        String idField2 = idField.getText();

        if (!Validation.isValidTime(idField2)) {
            status.setText("Neteisingai įvestas ID");
        } else {
            int idField3 = Integer.parseInt(idField.getText());
            BookDao.deleteById(idField3);
            status.setText("Pavyko sėkmingai ištrinti įraša");
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Pridedame reikšmes į ChoiceBox
        choiceBoxSummary.getItems().add("Puiku ");
        choiceBoxSummary.getItems().add("Gerai");
        choiceBoxSummary.getItems().add("Pusetinai");
        choiceBoxSummary.getItems().add("Blogai");
        choiceBoxSummary.getItems().add("Labai blogai");



        // Pažymėtos reikšmės
        checkBoxPoezija.setSelected(true);
       radioButtonJonas.setSelected(true);
        choiceBoxSummary.getSelectionModel().selectFirst();
    }
}