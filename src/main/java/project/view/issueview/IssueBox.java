package project.view.issueview;

import javafx.beans.property.SimpleStringProperty;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.input.*;
import javafx.scene.layout.VBox;
import project.controller.MainController;
import project.model.issue.Issue;

public class IssueBox extends VBox {

    public SimpleStringProperty placeProperty = new SimpleStringProperty();

    private static final String EPIC_STYLE = "-fx-background-color: green; -fx-border-color: white";
    private static final String STORY_STYLE = "-fx-background-color: lightblue; -fx-border-color: white";
    private static final String TASK_STYLE = "-fx-background-color: lightgrey; -fx-border-color: white";
    private static final String BAG_STYLE = "-fx-background-color: red; -fx-border-color: white";
    private static final String DRAG_STYLE = "-fx-background-color: lightgrey; -fx-border-color: black";

    private String currentStyle;

    private Issue issue;
    private Label labelType = new Label();
    private Label labelId = new Label();
    private Label labelTitle = new Label();
    private Label labelPriority = new Label();
    private Label labelStatus = new Label();

    public IssueBox(Issue issue, IssueState place, MainController mainController) {
        this.issue = issue;
        placeProperty.set(String.valueOf(place));
        setPrefSize(170, 90);

        switch (issue.getIssueType()) {
            case BUG:
                currentStyle = BAG_STYLE;
                break;
            case STORY:
                currentStyle = STORY_STYLE;
                break;
            case TASK:
                currentStyle = TASK_STYLE;
                break;
            default:
                currentStyle = EPIC_STYLE;
        }

        setStyle(currentStyle);
        setData();

        getChildren().addAll(labelType, labelId, labelTitle, labelPriority, labelStatus);

        setOnDragDetected(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                setStyle(DRAG_STYLE);
                ClipboardContent content = new ClipboardContent();
                content.putString(placeProperty.get());
                startDragAndDrop(TransferMode.ANY).setContent(content);
                event.consume();
            }
        });

        setOnDragDone(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                setStyle(currentStyle);
                if (event.getTransferMode() == TransferMode.MOVE)
                    placeProperty.set(event.getDragboard().getString());
                event.consume();
            }
        });

        setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getClickCount() == 2) {
                    new IssueForm((IssueBox) mouseEvent.getSource(),  mainController);
                    mouseEvent.consume();
                }
            }
        });
    }

    public Issue getIssue() {
        return issue;
    }

    public void setData() {
        labelType.setText(issue.getIssueType() + "");
        labelId.setText("ID " + issue.getId());
        labelTitle.setText(issue.getTitle());
        labelPriority.setText("Priority = " + issue.getPriority());
        labelStatus.setText(issue.getWorkFlowCurrentStatus()+"");
    }
}