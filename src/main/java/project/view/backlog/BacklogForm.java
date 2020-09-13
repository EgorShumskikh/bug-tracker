package project.view.backlog;

import javafx.event.EventHandler;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.FlowPane;
import project.view.issueview.IssueState;

public class BacklogForm extends FlowPane {

    private static final String NEW_STYLE = "-fx-border-color: black";

    private String oldStyle;

    public BacklogForm(IssueState s) {

        setOnDragOver(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                if (event.getGestureSource() != this) {
                    event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                }
                event.consume();
            }
        });

        setOnDragEntered(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                if (event.getGestureSource() != this) {
                    oldStyle = getStyle();
                    setStyle(NEW_STYLE);
                }
                event.consume();
            }
        });

        setOnDragExited(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                setStyle(oldStyle);
                event.consume();
            }
        });

        setOnDragDropped(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {

                Dragboard db = event.getDragboard();
                ClipboardContent content = new ClipboardContent();
                content.putString(String.valueOf(s));
                db.setContent(content);
                event.setDropCompleted(true);

                event.consume();
            }
        });
    }
}
