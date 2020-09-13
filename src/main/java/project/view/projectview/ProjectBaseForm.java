package project.view.projectview;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import project.controller.ProjectController;
import project.model.user.User;
import project.controller.MainController;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public abstract class ProjectBaseForm extends Application implements ProjectSaveChange {
    protected MainController mainController;
    protected ProjectController controller;
    protected Stage primaryStage = new Stage();

    public ProjectBaseForm(MainController mainController) {
        this.mainController = mainController;
        try {
            start(primaryStage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/project.fxml"));
        Scene scene = new Scene(loader.load());
        controller = loader.getController();
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.initModality(Modality.APPLICATION_MODAL);

        ObservableMap<Integer, String> users = FXCollections.observableMap(mainController.mainFactory.getUsers());
        controller.supervisorComboBox.getItems().setAll(users.entrySet());
        controller.adminComboBox.getItems().setAll(users.entrySet());
        controller.id.setDisable(true);

        controller.addMemberButton.setOnAction(event -> {
            if (!controller.teamMembersListView.getItems().containsAll(controller.myBox.findListView.getSelectionModel().getSelectedItems()))
                controller.teamMembersListView.getItems().addAll(controller.myBox.findListView.getSelectionModel().getSelectedItems());
        });

        controller.removeMemberButton.setOnAction(event -> {
            controller.teamMembersListView.getItems().removeAll(controller.teamMembersListView.getSelectionModel().getSelectedItems());
        });

        controller.myBox.setController(mainController.mainFactory);
        controller.myBox.setLeftAncors(0, 125, 125);
        controller.saveChangesButton.setOnAction(this::projectSaveChange);
        primaryStage.show();
    }

    @Override
    public void projectSaveChange(ActionEvent event) {
        mainController.menuEdit.setDisable(false);
        Map.Entry supervisorMapEntry = (Map.Entry) controller.supervisorComboBox.getSelectionModel().getSelectedItem();
        Map.Entry adminMapEntry = (Map.Entry) controller.adminComboBox.getSelectionModel().getSelectedItem();

        mainController.mainFactory.updateProgect(mainController.mainFactory.getCurrentProject(),
                controller.title.getText(),
                controller.description.getText(),
                controller.department.getText(),
                supervisorMapEntry == null ? null : mainController.mainFactory.getBdConnect().getUser((Integer) supervisorMapEntry.getKey()),
                adminMapEntry == null ? null : mainController.mainFactory.getBdConnect().getUser((Integer) adminMapEntry.getKey()));

        List<User> newUserlist = new ArrayList<>();

        //добавляем участников проекта
        for (int i = 0; i < controller.teamMembersListView.getItems().size(); i++) {
            Map.Entry teamMember = (Map.Entry) controller.teamMembersListView.getItems().get(i);
            User newUser = mainController.mainFactory.getUserFromMapEntry(teamMember);
            newUserlist.add(newUser);
            if (!mainController.mainFactory.getCurrentProject().getMembers().contains(newUser))
                mainController.mainFactory.addMemberToProject(newUser);
        }

        //удаляем участников проетка
        for (User e : mainController.mainFactory.getCurrentProject().getMembers()) {
            if (!newUserlist.contains(e))
                mainController.mainFactory.removeMemberFromProject(e);
        }

        mainController.refresh();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}