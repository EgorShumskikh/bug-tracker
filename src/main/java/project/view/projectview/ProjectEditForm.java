package project.view.projectview;

import project.controller.MainController;

import java.util.Map;

public class ProjectEditForm extends ProjectBaseForm {

    public ProjectEditForm(MainController mainController) {
        super(mainController);
        primaryStage.setTitle("Edit Project");
        controller.id.setText(mainController.mainFactory.getCurrentProject().getId() + "");
        controller.title.setText(mainController.mainFactory.getCurrentProject().getTitle());
        controller.description.setText(mainController.mainFactory.getCurrentProject().getDescription());
        controller.department.setText(mainController.mainFactory.getCurrentProject().getDepartmentName());

        Map.Entry<Integer, String> supervisor = mainController.mainFactory.getMapEntryFromUser(mainController.mainFactory.getCurrentProject().getSupervisor());
        if (supervisor != null) controller.supervisorComboBox.setValue(supervisor);

        Map.Entry<Integer, String> admin = mainController.mainFactory.getMapEntryFromUser(mainController.mainFactory.getCurrentProject().getAdmin());
        if (admin != null) controller.adminComboBox.setValue(admin);

        for (int i = 0; i < mainController.mainFactory.getCurrentProject().getMembers().size(); i++) {
            Map.Entry<Integer, String> teamMember = mainController.mainFactory.getMapEntryFromUser(mainController.mainFactory.getCurrentProject().getMembers().get(i));
            controller.teamMembersListView.getItems().add(teamMember);
        }
    }
}
