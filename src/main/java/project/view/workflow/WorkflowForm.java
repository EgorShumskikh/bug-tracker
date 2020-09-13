package project.view.workflow;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import project.controller.WorkflowController;
import project.model.factory.MainFactory;
import project.model.issue.Issue;

import project.model.workflow.WorkFlowStatus;
import project.controller.IssueController;

import java.io.IOException;

public class WorkflowForm extends Application {

    private Issue issue;
    private MainFactory mainFactory;
    IssueController issueController;

    public WorkflowForm(Issue issue, MainFactory mainFactory, IssueController issueController) {
        this.issue = issue;
        this.mainFactory = mainFactory;
        this.issueController = issueController;
        try {
            start(new Stage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/workflow.fxml"));
        Scene scene = new Scene(loader.load());
        WorkflowController workflowController = loader.getController();

        primaryStage = new Stage();
        primaryStage.setTitle("Workflow");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.initModality(Modality.APPLICATION_MODAL);

        ObservableList<WorkFlowStatus> currentStatuses = FXCollections.observableArrayList(issue.getWorkFlow().getWorkFlowList());
        workflowController.currentWorkflowList.getItems().addAll(currentStatuses);

        for (WorkFlowStatus value : WorkFlowStatus.values())
            if (!issue.getWorkFlow().getWorkFlowList().contains(value))
                workflowController.availableWorkflowList.getItems().add(value);

        workflowController.removeButton.setOnAction(event -> {
            if (workflowController.currentWorkflowList.getSelectionModel().getSelectedItem() == issue.getWorkFlowCurrentStatus()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Can't remove current status");
                alert.showAndWait();
                return;
            }
            if (workflowController.currentWorkflowList.getSelectionModel().getSelectedItem() != null) {
                mainFactory.removeStatusFromWorkFlow(issue.getWorkFlow(), (WorkFlowStatus) workflowController.currentWorkflowList.getSelectionModel().getSelectedItem());
                workflowController.availableWorkflowList.getItems().addAll(workflowController.currentWorkflowList.getSelectionModel().getSelectedItem());
                workflowController.currentWorkflowList.getItems().removeAll(workflowController.currentWorkflowList.getSelectionModel().getSelectedItem());
            }
        });

        workflowController.addButton.setOnAction(event -> {
            if (workflowController.availableWorkflowList.getSelectionModel().getSelectedItem() != null) {
                mainFactory.addStatusToWorkFlow(issue.getWorkFlow(), (WorkFlowStatus) workflowController.availableWorkflowList.getSelectionModel().getSelectedItem());
                workflowController.currentWorkflowList.getItems().addAll(workflowController.availableWorkflowList.getSelectionModel().getSelectedItem());
                workflowController.availableWorkflowList.getItems().removeAll(workflowController.availableWorkflowList.getSelectionModel().getSelectedItem());
            }
        });

        workflowController.okButton.setOnAction(event -> {
            ObservableList<WorkFlowStatus> workFlow = FXCollections.observableArrayList(issue.getWorkFlow().getWorkFlowList());
            SpinnerValueFactory<WorkFlowStatus> valueFactory = new SpinnerValueFactory.ListSpinnerValueFactory(workFlow);
            issueController.spinnerWorkFlow.getStyleClass().addAll(Spinner.STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL);
            issueController.spinnerWorkFlow.setValueFactory(valueFactory);
            issueController.spinnerWorkFlow.getValueFactory().setValue(issue.getWorkFlowCurrentStatus());

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
        });

        primaryStage.show();
    }
}