package project.model.issue.projectissue;

import project.model.issue.IssueDecomp;
import project.model.issue.Issue;
import project.model.issue.IssueType;

public class EpicIssue extends Issue implements IssueDecomp {
    public EpicIssue() {
        super(IssueType.EPIC);
    }

    @Override
    public IssueType getChildType() {
        return IssueType.STORY;
    }
}
