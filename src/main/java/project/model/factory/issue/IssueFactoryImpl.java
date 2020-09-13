package project.model.factory.issue;

import project.model.issue.Issue;
import project.model.issue.IssueType;
import project.model.issue.projectissue.BugIssue;
import project.model.issue.projectissue.EpicIssue;
import project.model.issue.projectissue.StoryIssue;
import project.model.issue.projectissue.TaskIssue;

public class IssueFactoryImpl implements IssueFactory {

    private static IssueFactoryImpl factory = new IssueFactoryImpl();

    public static IssueFactoryImpl getInstance() {
        return factory;
    }

    @Override
    public Issue createIssue(IssueType issueType) {
        Issue issue;

        switch (issueType) {
            case BUG:
                issue = new BugIssue();
                break;
            case EPIC:
                issue = new EpicIssue();
                break;
            case STORY:
                issue = new StoryIssue();
                break;
            case TASK:
                issue = new TaskIssue();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + issueType);
        }

        return issue;
    }
}
