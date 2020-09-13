package project.model.workflow;

public enum WorkFlowStatus {
    OPEN_ISSUE("Open Issue"),
    INPROGRESS_ISSUE("InProgress Issue"),
    REVIEW_ISSUE("Review Issue"),
    TEST_ISSUE("Test Issue"),
    RESOLVED_ISSUE("Resolved Issue"),
    REOPEND_ISSUE("ReOpend Issue"),
    CLOSE_ISSUE("Close Issue");

    private String title;
    WorkFlowStatus(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return title;
    }
}
