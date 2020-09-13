package project.view.userview;

import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import project.model.factory.MainFactory;

import java.util.*;

public class UserSearchForm extends AnchorPane {

    private MainFactory mainFactory;
    private Label findLabel;
    public ListView findListView;
    private TextField findTextField;

    public UserSearchForm() {
        super();
        setPrefSize(470, 110);

        findLabel = new Label("Find User");
        AnchorPane.setLeftAnchor(findLabel, 0d);

        findTextField = new TextField();
        AnchorPane.setLeftAnchor(findTextField, 125d);
        AnchorPane.setRightAnchor(findTextField, 15d);
        findTextField.setPromptText("enter ID or Name");

        findListView = new ListView();
        AnchorPane.setLeftAnchor(findListView, 0d);
        AnchorPane.setRightAnchor(findListView, 15d);
        AnchorPane.setTopAnchor(findListView, 40d);
        AnchorPane.setBottomAnchor(findListView, 0d);

        getChildren().addAll(findLabel, findTextField, findListView);
    }

    public void setLeftAncors(double labelAnchor, double textFieldAnchor, double listViewAnchor) {
        AnchorPane.setLeftAnchor(findLabel, labelAnchor);
        AnchorPane.setLeftAnchor(findTextField, textFieldAnchor);
        AnchorPane.setLeftAnchor(findListView, listViewAnchor);
    }

    public void setController(MainFactory mainFactory) {

        findTextField.textProperty().addListener((observableValue, oldText, newText) -> {
            if (!newText.isEmpty()) {
                findListView.getItems().clear();
                Map<Integer, String> findusers = findUserByIdOrName(findTextField.getText(), mainFactory.getUsers());

                findListView.getItems().addAll(findusers.entrySet());
            }
        });
    }

    private Map<Integer, String> findUserByIdOrName(String user, Map<Integer, String> users) throws NumberFormatException {
        try {
            Map<Integer, String> result = new HashMap<>();
            if (users.get(Integer.parseInt(user)) != null)
                result.put(Integer.parseInt(user), users.get(Integer.parseInt(user)));

            return result;
        } catch (Exception e) {

            Map<Integer, String> result = new HashMap<>();
            users.entrySet().forEach(n -> {
                if (n.getValue().indexOf(user) != -1) result.put(n.getKey(), n.getValue());
            });

            return result;
        }
    }
}