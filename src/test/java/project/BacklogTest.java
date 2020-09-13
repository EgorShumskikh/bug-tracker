package project;

import project.model.backlog.BaseScrumEntity;
import project.model.backlog.ProjectBacklog;
import project.model.issue.Issue;
import project.model.issue.projectissue.StoryIssue;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class BacklogTest {

    @Test
    public void tes_backlog_addIssue() {
        BaseScrumEntity backlog = new ProjectBacklog();
        Issue issue = new StoryIssue();
        backlog.addIssue(issue);
        assertThat(backlog.getIssueList(), contains(issue));
    }

    @Test
    public void tes_backlog_addIssue_no_dublicates() {
        BaseScrumEntity backlog = new ProjectBacklog();
        Issue issue = new StoryIssue();
        backlog.addIssue(issue);
        backlog.addIssue(issue);
        backlog.addIssue(issue);
        assertThat(backlog.getIssueList(), hasSize(1));
    }
}
