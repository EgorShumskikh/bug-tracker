package project;

import project.model.backlog.ProjectSprint;
import project.model.factory.MainFactory;
import project.controller.SingleController;
import project.model.issue.Issue;
import project.model.issue.IssuePriority;
import project.model.issue.IssueType;
import project.model.project.Project;
import project.model.project.ScrumProject;
import project.model.user.User;
import org.junit.Before;
import org.junit.Test;
import project.model.workflow.WorkFlow;
import project.model.workflow.WorkFlowStatus;

import java.time.LocalDate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MainFactoryTest {

    private MainFactory mainFactory;
    private User user;

    @Before
    public void setUp() {
        mainFactory = SingleController.getInstance();
        user = mainFactory.createUser(null, "Иванов", null);
    }

    @Test
    public void tes_projectController_createUser() {
        mainFactory.createUser("Иван", "Иванов", "Иванович");
        mainFactory.createUser("Максим", "Петров", "Петрович");
        mainFactory.createUser("Алексей", "Сидоров", "Максимович");

        assertThat(mainFactory.getUsers(), hasValue("Петров Максим Петрович"));
    }

    @Test
    public void tes_projectController_createWorkflow() {
        mainFactory.createWorkflow("Workflow 1");
        mainFactory.createWorkflow("Workflow 2");
        mainFactory.createWorkflow("Workflow 3");

        assertThat(mainFactory.getWorkflows(), hasValue("Workflow 2"));
    }


    @Test
    public void tes_projectController_addMemberToProject() {
        Project project = mainFactory.createProject("Проект №1", "Описание проекта №1", "Подразделение №1", null, null);
        User user = mainFactory.createUser("Иван", "Иванов", "Иванович");
        project.addMember(user);

        assertThat(project.getMembers(), hasItem(user));
    }

    @Test
    public void tes_projectController_addMemberToProject_correct_size_after_add() {
        Project project = mainFactory.createProject("Проект №1", "Описание проекта №1", "Подразделение №1", null, null);
        User user = mainFactory.createUser("Иван", "Иванов", "Иванович");
        project.addMember(user);
        project.addMember(user);
        project.addMember(user);

        assertThat(project.getMembers(), hasSize(1));
    }

    @Test
    public void tes_projectController_removeMemberFromProject() {
        Project project = mainFactory.createProject("Проект №1", "Описание проекта №1", "Подразделение №1", null, null);
        User user1 = mainFactory.createUser("Иван", "Иванов", "Иванович");
        User user2 = mainFactory.createUser("Алексей", "Сидоров", "Максимович");
        project.addMember(user1);
        project.addMember(user2);
        project.removeMember(user1);
        project.removeMember(user2);

        assertThat(project.getMembers(), not(contains(user1, user2)));
    }

    @Test
    public void tes_projectController_removeMemberFromProject_correct_size_after_remove() {
        Project project = mainFactory.createProject("Проект №1", "Описание проекта №1", "Подразделение №1", null, null);
        User user1 = mainFactory.createUser("Иван", "Иванов", "Иванович");
        User user2 = mainFactory.createUser("Алексей", "Сидоров", "Максимович");
        project.addMember(user1);
        project.addMember(user2);
        project.removeMember(user1);
        project.removeMember(user1);
        project.removeMember(user1);

        assertThat(project.getMembers(), hasSize(1));
    }

    @Test
    public void tes_projectController_createProject() {
        mainFactory.createProject("Проект №1", "Описание проекта №1", "Подразделение №1", null, null);

        assertThat(mainFactory.getProjects(), hasValue("Проект №1"));
    }

    @Test
    public void tes_projectController_createProject_correct_currentProject() {
        Project project1 = mainFactory.createProject("Проект №1", "Описание проекта №1", "Подразделение №1", null, null);
        Project project2 = mainFactory.createProject("Проект №1", "Описание проекта №1", "Подразделение №1", null, null);

        assertTrue(mainFactory.getCurrentProject() == project2);
    }

    @Test
    public void tes_projectController_createSprintInProject() {
        Project project = mainFactory.createProject("Проект №1", "Описание проекта №1", "Подразделение №1", null, null);
        ProjectSprint sprint1 = mainFactory.createProjectSprint("Sprint1", LocalDate.of(2011, 11, 1), LocalDate.of(2011, 11, 1));
        ProjectSprint sprint2 = mainFactory.createProjectSprint("Sprint2", LocalDate.of(2011, 11, 1), LocalDate.of(2011, 11, 1));

        assertThat(mainFactory.getCurrentProject().getSprints(), contains(sprint1, sprint2));
    }

    @Test
    public void tes_projectController_createSprintInProject_correct_current_sprint() {
        Project project = mainFactory.createProject("Проект №1", "Описание проекта №1", "Подразделение №1", null, null);
        ProjectSprint sprint1 = mainFactory.createProjectSprint("Sprint1", LocalDate.of(2011, 11, 1), LocalDate.of(2011, 11, 1));
        ProjectSprint sprint2 = mainFactory.createProjectSprint("Sprint2", LocalDate.of(2011, 11, 1), LocalDate.of(2011, 11, 1));

        assertTrue(mainFactory.getCurrentProject().getCurrentSprint() == sprint2);
    }

    @Test
    public void tes_projectController_createIssueInBacklog() {
        ScrumProject project = mainFactory.createProject("Проект №1", "Описание проекта №1", "Подразделение №1", null, null);
        Issue issue1 = mainFactory.createIssueInBacklog(IssueType.EPIC, "Главная задача", null, null, null, IssuePriority.HIGH, null, user, null, null);
        Issue issue2 = mainFactory.createIssueInBacklog(IssueType.BUG, "Задача2", null, null, null, IssuePriority.HIGH, null, user, null, null);

        assertThat(project.getProjectBacklog().getIssueList(), contains(issue1, issue2));
    }

    @Test
    public void tes_projectController_createChildIssue() {
        mainFactory.createProject("Проект №1", "Описание проекта №1", "Подразделение №1", null, null);
        Issue issue1 = mainFactory.createIssueInBacklog(IssueType.EPIC, "Задача1", null, null, null, IssuePriority.HIGH, null, user, null, null);
        Issue issue2 = mainFactory.createChildIssue(issue1, "Подзадача");

        assertTrue(issue2.getParentIsue() == issue1);
    }

    @Test
    public void tes_projectController_deleteIssueFromProject_delete_from_backlog() {
        ScrumProject project = mainFactory.createProject("Проект №1", "Описание проекта №1", "Подразделение №1", null, null);
        Issue issue = mainFactory.createIssueInBacklog(IssueType.EPIC, "Задача1", null, null, null, IssuePriority.HIGH, null, user, null, null);
        mainFactory.deleteIssueFromProject(issue);

        assertThat(project.getProjectBacklog().getIssueList(), not(contains(issue)));
    }

    @Test
    public void tes_projectController_deleteIssueFromProject_delete_from_sprint() {
        ScrumProject project = mainFactory.createProject("Проект №1", "Описание проекта №1", "Подразделение №1", null, null);
        mainFactory.createProjectSprint("Спринт1", LocalDate.of(2011, 11, 1), LocalDate.of(2011, 11, 1));
        Issue issue = mainFactory.createIssueInBacklog(IssueType.EPIC, "Задача1", null, null, null, IssuePriority.HIGH, null, user, null, null);
        mainFactory.createProjectSprint("Спринт2", LocalDate.of(2011, 11, 1), LocalDate.of(2011, 11, 1));
        mainFactory.moveIssueToSprint(issue);
        mainFactory.deleteIssueFromProject(issue);

        assertThat(project.getCurrentSprint().getIssueList(), not(contains(issue)));
    }

    @Test
    public void tes_projectController_moveIssueToSprint_sprint_contains_issue() {
        ScrumProject project = mainFactory.createProject("Проект №1", "Описание проекта №1", "Подразделение №1", null, null);
        mainFactory.createProjectSprint("Спринт1", LocalDate.of(2011, 11, 1), LocalDate.of(2011, 11, 1));
        Issue issue = mainFactory.createIssueInBacklog(IssueType.EPIC, "Задача1", null, null, null, IssuePriority.HIGH, null, user, null, null);
        mainFactory.moveIssueToSprint(issue);

        assertThat(project.getCurrentSprint().getIssueList(), contains(issue));
    }

    @Test
    public void tes_projectController_moveIssueToSprint_backlog_not_contains_issue() {
        ScrumProject project = mainFactory.createProject("Проект №1", "Описание проекта №1", "Подразделение №1", null, null);
        mainFactory.createProjectSprint("Спринт1", LocalDate.of(2011, 11, 1), LocalDate.of(2011, 11, 1));
        Issue issue = mainFactory.createIssueInBacklog(IssueType.EPIC, "Задача1", null, null, null, IssuePriority.HIGH, null, user, null, null);
        mainFactory.moveIssueToSprint(issue);

        assertThat(project.getProjectBacklog().getIssueList(), not(contains(issue)));
    }

    @Test
    public void tes_projectController_moveIssueToBacklog_backlog_contains_issue() {
        ScrumProject project = mainFactory.createProject("Проект №1", "Описание проекта №1", "Подразделение №1", null, null);
        mainFactory.createProjectSprint("Спринт1", LocalDate.of(2011, 11, 1), LocalDate.of(2011, 11, 1));
        Issue issue = mainFactory.createIssueInBacklog(IssueType.EPIC, "Задача1", null, null, null, IssuePriority.HIGH, null, user, null, null);
        mainFactory.moveIssueToSprint(issue);
        mainFactory.moveIssueToBacklog(issue);

        assertThat(project.getProjectBacklog().getIssueList(), contains(issue));
    }

    @Test
    public void tes_projectController_moveIssueToBacklog_sprint_not_contains_issue() {
        ScrumProject project = mainFactory.createProject("Проект №1", "Описание проекта №1", "Подразделение №1", null, null);
        mainFactory.createProjectSprint("Спринт1", LocalDate.of(2011, 11, 1), LocalDate.of(2011, 11, 1));
        Issue issue = mainFactory.createIssueInBacklog(IssueType.EPIC, "Задача1", null, null, null, IssuePriority.HIGH, null, user, null, null);
        mainFactory.moveIssueToSprint(issue);
        mainFactory.moveIssueToBacklog(issue);

        assertThat(project.getCurrentSprint().getIssueList(), not(contains(issue)));
    }

    @Test
    public void tes_projectController_setNextIssueStatus() {
        WorkFlow newWorkFlow = mainFactory.createWorkflow("Workflow1");
        mainFactory.addStatusToWorkFlow(newWorkFlow, WorkFlowStatus.OPEN_ISSUE);
        mainFactory.addStatusToWorkFlow(newWorkFlow, WorkFlowStatus.REVIEW_ISSUE);
        mainFactory.addStatusToWorkFlow(newWorkFlow, WorkFlowStatus.CLOSE_ISSUE);
        Issue issue = mainFactory.createIssueInBacklog(IssueType.EPIC, "Задача1", null, null, newWorkFlow, IssuePriority.HIGH, null, user, null, WorkFlowStatus.OPEN_ISSUE);

        mainFactory.setNextIssueStatus(issue);

        assertEquals(issue.getWorkFlowCurrentStatus(), WorkFlowStatus.REVIEW_ISSUE);
    }

    @Test
    public void tes_projectController_setPreviousIssueStatus() {
        WorkFlow newWorkFlow = mainFactory.createWorkflow("Workflow1");
        mainFactory.addStatusToWorkFlow(newWorkFlow, WorkFlowStatus.OPEN_ISSUE);
        mainFactory.addStatusToWorkFlow(newWorkFlow, WorkFlowStatus.REVIEW_ISSUE);
        mainFactory.addStatusToWorkFlow(newWorkFlow, WorkFlowStatus.CLOSE_ISSUE);
        Issue issue = mainFactory.createIssueInBacklog(IssueType.EPIC, "Задача1", null, null, newWorkFlow, IssuePriority.HIGH, null, user, null, WorkFlowStatus.CLOSE_ISSUE);
        mainFactory.setPreviousIssueStatus(issue);
        mainFactory.setPreviousIssueStatus(issue);

        assertEquals(issue.getWorkFlowCurrentStatus(), WorkFlowStatus.OPEN_ISSUE);
    }
}