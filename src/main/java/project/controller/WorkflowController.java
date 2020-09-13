package project.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

import java.net.URL;
import java.util.ResourceBundle;

public class WorkflowController implements Initializable {
    @FXML
    public ListView currentWorkflowList, availableWorkflowList;

    @FXML
    public Button addButton, removeButton, okButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) { }
}
