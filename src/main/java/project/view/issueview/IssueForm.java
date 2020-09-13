package project.view.issueview;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import project.controller.IssueController;
import project.model.factory.MainFactory;
import project.model.issue.Issue;
import project.model.issue.IssuePriority;
import project.model.workflow.WorkFlowStatus;
import project.controller.MainController;
import project.view.workflow.WorkflowForm;

import java.util.Map;

public class IssueForm extends Application {

    private IssueBox issuePanel;
    private MainFactory mainFactory;
    private IssueController controller;
    private MainController mainController;

    public IssueForm(IssueBox issuePanel, MainController mainController) {
        this.issuePanel = issuePanel;
        this.mainFactory = mainController.mainFactory;
        this.mainController = mainController;

        try {
            start(new Stage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/issue.fxml"));
        Scene scene = new Scene(loader.load());
        controller = loader.getController();

        primaryStage = new Stage();

        primaryStage.setTitle("Issue Detail");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.initModality(Modality.APPLICATION_MODAL);
        primaryStage.show();

        ObservableList<IssuePriority> types = FXCollections.observableArrayList(IssuePriority.values());
        controller.priority.getItems().setAll(types);

        ObservableMap<Integer, String> users = FXCollections.observableMap(mainFactory.getUsers());
        controller.executor.getItems().setAll(users.entrySet());
        controller.reporter.getItems().setAll(users.entrySet());

        ObservableList<WorkFlowStatus> workFlow = FXCollections.observableArrayList(issuePanel.getIssue().getWorkFlow().getWorkFlowList());
        SpinnerValueFactory<WorkFlowStatus> valueFactory = new SpinnerValueFactory.ListSpinnerValueFactory(workFlow);
        controller.spinnerWorkFlow.getStyleClass().addAll(Spinner.STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL);
        controller.spinnerWorkFlow.setValueFactory(valueFactory);
        controller.spinnerWorkFlow.getValueFactory().setValue(issuePanel.getIssue().getWorkFlowCurrentStatus());

        controller.id.setText(issuePanel.getIssue().getId() + "");
        controller.title.setText(issuePanel.getIssue().getTitle());
        controller.description.setText(issuePanel.getIssue().getDescription());
        controller.type.setText(issuePanel.getIssue().getIssueType().name());
        controller.priority.setValue(issuePanel.getIssue().getPriority());
        if (issuePanel.getIssue().getParentIsue() != null)
            controller.parentIssueText.setText(issuePanel.getIssue().getParentIsue().getId() + " - " + issuePanel.getIssue().getParentIsue().getTitle());


        Map.Entry<Integer, String> executor = mainFactory.getMapEntryFromUser(issuePanel.getIssue().getExecutor());
        if (executor != null) controller.executor.setValue(executor);

        Map.Entry<Integer, String> reporter = mainFactory.getMapEntryFromUser(issuePanel.getIssue().getReporter());
        if (reporter != null) controller.reporter.setValue(reporter);

        controller.creationDate.setValue(issuePanel.getIssue().getCreationDate());

        controller.saveChanges.setOnAction(event -> {
            mainFactory.updateIssue(issuePanel.getIssue(),
                    issuePanel.getIssue().getIssueType(),
                    controller.title.getText(),
                    issuePanel.getIssue().getParentIsue(),
                    controller.description.getText(),
                    issuePanel.getIssue().getWorkFlow(),
                    (IssuePriority) controller.priority.getValue(),
                    controller.creationDate.getValue(),
                    mainFactory.getUserFromMapEntry((Map.Entry) controller.executor.getValue()),
                    mainFactory.getUserFromMapEntry((Map.Entry) controller.reporter.getValue()),
                    (WorkFlowStatus) controller.spinnerWorkFlow.getValue());

            issuePanel.setData();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
        });

        controller.deleteIssue.setOnAction(event -> {
            if (mainController.mainFactory.getBdConnect().isparent(issuePanel.getIssue())) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Can't remove parent issue");
                alert.showAndWait();
            } else {
                issuePanel.placeProperty.set(String.valueOf(IssueState.DELETE));

                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.close();
            }
        });

        controller.setWorkflowButton.setOnAction(event -> {
            new WorkflowForm(issuePanel.getIssue(), mainFactory, controller);
        });

        controller.createChildButton.setOnAction(event -> {
            Issue issue = mainFactory.createChildIssue(issuePanel.getIssue(), "child of " + issuePanel.getIssue().getId());
            mainController.newTask(issue, IssueState.BACKLOG);
        });
    }
}