package project.model.issue.projectissue;

import project.model.issue.IssueDecomp;
import project.model.issue.Issue;
import project.model.issue.IssueType;

public class TaskIssue extends Issue implements IssueDecomp {
    public TaskIssue() {
        super(IssueType.TASK);
    }

    @Override
    public IssueType getChildType() {
        return IssueType.BUG;
    }
}
