package project.model.issue.projectissue;

import project.model.issue.IssueDecomp;
import project.model.issue.Issue;
import project.model.issue.IssueType;

public class StoryIssue extends Issue implements IssueDecomp {
    public StoryIssue() {
        super(IssueType.STORY);
    }

    @Override
    public IssueType getChildType() {
        return IssueType.TASK;
    }
}
