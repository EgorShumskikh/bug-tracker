package project.view.userview;

import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import project.model.user.User;
import project.controller.MainController;
import project.view.SelectForm;

import java.util.Map;

public class UserSelectForm extends SelectForm {

    public UserSelectForm(String title, MainController mainController) {
        super(title, mainController);
    }

    @Override
    public void setListView(){
        ObservableMap<Integer, String> users = FXCollections.observableMap(mainController.mainFactory.getUsers());
        selectListView.getItems().addAll(users.entrySet());
    }

    @Override
    public void selectAction() {
        if (!selectListView.getSelectionModel().isEmpty()) {
            int selectedUserId = (int) ((Map.Entry) selectListView.getSelectionModel().getSelectedItem()).getKey();
            User user = mainController.mainFactory.getBdConnect().getUser(selectedUserId);
            new UserEditForm(mainController.mainFactory, user);
        }
    }
}
