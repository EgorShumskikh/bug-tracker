package project.model.backlog;

import project.model.BaseEntity;
import project.model.issue.Issue;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class BaseScrumEntity extends BaseEntity {

    private String title;
    private List<Issue> issueList;

    public BaseScrumEntity() {
        this.issueList = new ArrayList<>();
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public List<Issue> getIssueList() {
        return issueList.stream().collect(Collectors.toList());
    }

    public void setIssueList(List<Issue> issueList) {
        this.issueList = issueList;
    }

    public boolean addIssue(Issue issue) {

        if (!issueList.contains(issue))
            return issueList.add(issue);
        else
            return false;
    }

    public boolean removeIssue(Issue issue) {
        return issueList.remove(issue);
    }
}