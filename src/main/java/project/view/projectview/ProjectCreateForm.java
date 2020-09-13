package project.view.projectview;

import javafx.event.ActionEvent;
import project.controller.MainController;

public class ProjectCreateForm extends ProjectBaseForm {

    public ProjectCreateForm(MainController mainController) {
        super(mainController);
        primaryStage.setTitle("New Project");
        controller.saveChangesButton.setText("Create Project");
    }

    @Override
    public void projectSaveChange(ActionEvent event) {
        mainController.mainFactory.createProject(null, null, null, null, null);
        super.projectSaveChange(event);
    }
}
