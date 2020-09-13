package project.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import project.view.userview.UserSearchForm;

import java.net.URL;
import java.util.ResourceBundle;

public class ProjectController implements Initializable {

    @FXML
    public Button saveChangesButton, addMemberButton, removeMemberButton;

    @FXML
    public TextField id, title, description, department;

    @FXML
    public ComboBox supervisorComboBox, adminComboBox;

    @FXML
    public ListView teamMembersListView;

    @FXML
    public UserSearchForm myBox;

    @Override
    public void initialize(URL location, ResourceBundle resources) {    }
}
