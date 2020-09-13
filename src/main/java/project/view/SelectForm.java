package project.view;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import project.controller.MainController;

public abstract class SelectForm extends Application {
    protected MainController mainController;
    protected ListView selectListView = new ListView<>();
    protected String title;

    public SelectForm(String title, MainController mainController) {
        this.title = title;
        this.mainController = mainController;
        try {
            start(new Stage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start(Stage primaryStage) {
        Button openButton = new Button("Select");
        VBox root = new VBox(10, selectListView, openButton);
        root.setAlignment(Pos.CENTER);
        VBox.setMargin(openButton, new Insets(10));
        VBox.setMargin(selectListView, new Insets(10));

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.initModality(Modality.APPLICATION_MODAL);
        primaryStage.setWidth(500);
        primaryStage.setHeight(400);
        primaryStage.setTitle(title);

        setListView();

        openButton.setOnAction(event -> {
            selectAction();
            primaryStage.close();
        });
        primaryStage.show();
    }

    public void setListView() {    }

    public void selectAction() {    }
}