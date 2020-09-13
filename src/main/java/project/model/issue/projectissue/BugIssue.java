package project.model.issue.projectissue;

import project.model.issue.IssueDecomp;
import project.model.issue.Issue;
import project.model.issue.IssueType;

public class BugIssue extends Issue implements IssueDecomp {
    public BugIssue() { super(IssueType.BUG); }

    @Override
    public IssueType getChildType() {
        return IssueType.BUG;
    }
}
