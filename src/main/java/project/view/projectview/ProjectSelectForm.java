package project.view.projectview;

import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import project.model.project.ScrumProject;
import project.controller.MainController;
import project.view.SelectForm;

import java.util.Map;

public class ProjectSelectForm extends SelectForm {

    public ProjectSelectForm(String title, MainController mainController) {
        super(title, mainController);
    }

    @Override
    public void setListView(){
        ObservableMap<Integer, String> projects = FXCollections.observableMap(mainController.mainFactory.getProjects());
        selectListView.getItems().addAll(projects.entrySet());
    }

    @Override
    public void selectAction() {
        if (!selectListView.getSelectionModel().isEmpty()) {
            int selectedProjectId = (int) ((Map.Entry) selectListView.getSelectionModel().getSelectedItem()).getKey();
            ScrumProject project1 = mainController.mainFactory.loadProject( selectedProjectId);
            mainController.mainFactory.setCurrentProject(project1);
            mainController.refresh();
        }
    }
}
