package project;

import project.model.backlog.ProjectBacklog;
import project.model.backlog.ProjectSprint;
import project.model.issue.Issue;
import project.model.issue.projectissue.BugIssue;
import project.model.project.ScrumProject;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


public class ScrumProjectTest {

    private ScrumProject project;
    private List<ProjectSprint> projectSprintList;
    private ProjectSprint projectSprint = new ProjectSprint();
    private ProjectBacklog projectBacklog = new ProjectBacklog();
    private Issue issue;

    @Before
    public void setUp() {
        project = new ScrumProject();
        projectSprintList = new ArrayList<>();
        projectSprintList.add(projectSprint);
        projectSprintList.add(new ProjectSprint());
        projectSprintList.add(new ProjectSprint());
        project.setSprints(projectSprintList);
        issue = new BugIssue();
        project.setCurrentSprint(projectSprint);
        project.setProjectBacklog(projectBacklog);
    }

    @Test
    public void tes_ScrumProject_getSprints_equal_original() {
        project.getSprints();

        assertThat(project.getSprints(), equalTo(projectSprintList));
    }

    @Test
    public void tes_ScrumProject_getSprints_cant_edit_original() {
        List<ProjectSprint> newProjectSprintList = project.getSprints();
        newProjectSprintList.add(new ProjectSprint());

        assertThat(newProjectSprintList, not(equalTo(projectSprintList)));
    }

    @Test
    public void tes_ScrumProject_moveIssuToSprint_issue_move_to_sprint() {
        project.getProjectBacklog().addIssue(issue);
        project.moveIssueToSprint(issue);

        assertThat(project.getCurrentSprint().getIssueList(), hasItem(issue));
    }

    @Test
    public void tes_ScrumProject_moveIssuToSprint_issue_delete_from_backlog() {
        project.getProjectBacklog().addIssue(issue);
        project.moveIssueToSprint(issue);

        assertThat(project.getProjectBacklog().getIssueList(), not(hasItem(issue)));
    }

    @Test
    public void tes_ScrumProject_moveIssuToBacklog_issue_move_to_backlog() {
        project.getCurrentSprint().addIssue(issue);
        project.moveIssueToBacklog(issue);

        assertThat(project.getProjectBacklog().getIssueList(), hasItem(issue));
    }

    @Test
    public void tes_ScrumProject_moveIssuToBacklog_issue_delete_from_backlog() {
        project.getProjectBacklog().addIssue(issue);
        project.moveIssueToBacklog(issue);

        assertThat(project.getCurrentSprint().getIssueList(), not(hasItem(issue)));
    }
}
