package project.model.issue;

import project.model.factory.issue.IssueFactory;
import project.model.factory.issue.IssueFactoryImpl;
import project.model.BaseEntity;
import project.model.user.User;
import project.model.workflow.WorkFlow;
import project.model.workflow.WorkFlowStatus;

import java.time.LocalDate;
import java.util.Objects;

public abstract class Issue extends BaseEntity implements IssueDecomp {
    protected IssueFactory issueFactory = IssueFactoryImpl.getInstance();
    protected IssueType issueType;
    private String title;
    private Issue parentIsue;
    private String description;
    private WorkFlow workFlow;
    private IssuePriority priority;
    private LocalDate creationDate;
    private User executor;
    private User reporter;
    private WorkFlowStatus workFlowCurrentStatus;

    public Issue(IssueType issueType) {
        this.issueType = issueType;
    }

    public IssueType getIssueType() {
        return issueType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public IssuePriority getPriority() {
        return priority;
    }

    public void setPriority(IssuePriority priority) {
        this.priority = priority;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public User getExecutor() {
        return executor;
    }

    public void setExecutor(User executor) {
        this.executor = executor;
    }

    public User getReporter() {
        return reporter;
    }

    public void setReporter(User reporter) {
        this.reporter = reporter;
    }

    public Issue getParentIsue() {
        return parentIsue;
    }

    public void setParentIsue(Issue parentIsue) {
        this.parentIsue = parentIsue;
    }

    public WorkFlow getWorkFlow() {
        return workFlow;
    }

    public void setWorkFlow(WorkFlow workFlow) {
        this.workFlow = workFlow;
    }

    public WorkFlowStatus getWorkFlowCurrentStatus() {
        return workFlowCurrentStatus;
    }

    public void setWorkFlowCurrentStatus(WorkFlowStatus workFlowCurrentStatus) {
        this.workFlowCurrentStatus = workFlowCurrentStatus;
    }

    public void setAllIssueAttributes(IssueType issueType, String title, Issue parentIsue, String description, WorkFlow workFlow, IssuePriority priority,
                                      LocalDate creationDate, User executor, User reporter, WorkFlowStatus workFlowCurrentStatus) {
        this.issueType = issueType;
        this.title = title;
        this.parentIsue = parentIsue;
        this.description = description;
        this.workFlow = workFlow;
        this.priority = priority;
        this.creationDate = creationDate;
        this.executor = executor;
        this.reporter = reporter;
        this.workFlowCurrentStatus = workFlowCurrentStatus;
    }

    public WorkFlowStatus setNextStatus() {
        for (int i = 0; i < workFlow.getWorkFlowList().size() - 1; i++)
            if (workFlow.getWorkFlowList().get(i) == workFlowCurrentStatus) {
                workFlowCurrentStatus = workFlow.getWorkFlowList().get(i + 1);
                break;
            }
        return workFlowCurrentStatus;
    }

    public WorkFlowStatus setPreviousStatus() {
        for (int i = 1; i < workFlow.getWorkFlowList().size(); i++)
            if (workFlow.getWorkFlowList().get(i) == workFlowCurrentStatus) {
                workFlowCurrentStatus = workFlow.getWorkFlowList().get(i - 1);
                break;
            }
        return workFlowCurrentStatus;
    }

    @Override
    public String toString() {
        return "Issue{" +
                "id=" + id +
                ", issueType=" + issueType +
                ", title='" + title + '\'' +
                ", parentIsue=" + parentIsue +
                ", description='" + description + '\'' +
                ", current workFlow status=" + workFlowCurrentStatus +
                ", priority=" + priority +
                ", creationDate=" + creationDate +
                ", executor='" + executor + '\'' +
                ", reporter='" + reporter + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Issue)) return false;
        Issue issue = (Issue) o;

        return id == issue.id &&
                Objects.equals(issueFactory, issue.issueFactory) &&
                issueType == issue.issueType &&
                Objects.equals(title, issue.title) &&
                Objects.equals(parentIsue, issue.parentIsue) &&
                Objects.equals(description, issue.description) &&
                Objects.equals(workFlow, issue.workFlow) &&
                priority == issue.priority &&
                Objects.equals(creationDate, issue.creationDate) &&
                Objects.equals(executor, issue.executor) &&
                Objects.equals(reporter, issue.reporter) &&
                workFlowCurrentStatus == issue.workFlowCurrentStatus;
    }
}