package project.view.userview;

import javafx.event.ActionEvent;
import project.model.factory.MainFactory;

public class UserCreateForm extends UserBaseForm {


    public UserCreateForm(MainFactory mainFactory) {
        super("New User", mainFactory);
    }

    @Override
    public void saveChangeAction(ActionEvent event) {
        mainFactory.createUser(
                userController.firstName.getText(),
                userController.lastName.getText(),
                userController.patronymic.getText());

        super.saveChangeAction(event);
    }
}
