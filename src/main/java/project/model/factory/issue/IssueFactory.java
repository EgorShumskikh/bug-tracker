package project.model.factory.issue;

import project.model.issue.Issue;
import project.model.issue.IssueType;

public interface IssueFactory {

    Issue createIssue(IssueType issueType);
}
