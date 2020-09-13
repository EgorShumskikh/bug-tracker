package project.view.userview;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import project.controller.UserController;
import project.model.factory.MainFactory;

import java.io.IOException;

public abstract class UserBaseForm extends Application {

    protected MainFactory mainFactory;
    protected UserController userController;
    private String title;

    public UserBaseForm(String title, MainFactory mainFactory) {
        this.mainFactory = mainFactory;
        this.title = title;

        try {
            start(new Stage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/user.fxml"));
        Scene scene = new Scene(loader.load());
        userController = loader.getController();

        primaryStage = new Stage();
        primaryStage.setTitle(title);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.initModality(Modality.APPLICATION_MODAL);

        primaryStage.show();

        userController.okButton.setOnAction(event -> saveChangeAction(event));
    }

    public void saveChangeAction(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}
