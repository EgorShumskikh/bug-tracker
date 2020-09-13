package project.view.userview;

import javafx.event.ActionEvent;
import project.model.factory.MainFactory;
import project.model.user.User;

public class UserEditForm extends UserBaseForm {
    private User user;

    public UserEditForm(MainFactory mainFactory, User user) {
        super("Edit User", mainFactory);
        this.user=user;
        userController.id.setText(user.getId()+"");
        userController.firstName.setText(user.getFirstName());
        userController.lastName.setText(user.getLastName());
        userController.patronymic.setText(user.getPatronymic());
    }

    @Override
    public void saveChangeAction(ActionEvent event) {
        mainFactory.updateUser(user, userController.firstName.getText(), userController.lastName.getText(), userController.patronymic.getText());

        super.saveChangeAction(event);
    }
}
