package project.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import java.net.URL;
import java.util.ResourceBundle;

public class IssueController implements Initializable {

    @FXML
    public TextField id, title, type, parentIssueText;

    @FXML
    public DatePicker creationDate;

    @FXML
    public TextArea description;

    @FXML
    public ChoiceBox priority, executor, reporter;

    @FXML
    public Button saveChanges, deleteIssue, setWorkflowButton, createChildButton;

    @FXML
    public Spinner spinnerWorkFlow;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }
}