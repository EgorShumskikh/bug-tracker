package project.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class UserController implements Initializable {

    @FXML
    public Button okButton;

    @FXML
    public TextField id, firstName, lastName, patronymic;

    @Override
    public void initialize(URL location, ResourceBundle resources) {    }
}
