package project.task;

import project.model.backlog.ProjectSprint;
import project.filters.ChainOfFilters;
import project.filters.FilterType;
import project.filters.Request;
import project.model.factory.MainFactory;
import project.controller.SingleController;
import project.model.issue.Issue;
import project.model.issue.IssuePriority;
import project.model.issue.IssueType;
import project.model.project.ScrumProject;
import project.model.user.User;
import project.model.workflow.WorkFlow;
import project.model.workflow.WorkFlowStatus;

import java.time.LocalDate;
import java.util.List;

public class Test {
    public static void main(String[] args) {
        MainFactory workSpace = SingleController.getInstance();
        ChainOfFilters chain = new ChainOfFilters();
        Request request;

        // 1) Я как пользователь хочу создавать отдельное пространство для работы над конкретным проектом.
        User user1 = workSpace.createUser(null, "Petrov", null);
        workSpace.updateUser(user1, "Petr", null, "Petrovich");
        User user2 = workSpace.createUser(null, "Ivanov", null);
        User user3 = workSpace.createUser(null, "Sidorov", null);

        ScrumProject project1 = workSpace.createProject("Project #1", "Project #1 description", "Department #1", user1, null);
        workSpace.updateProgect(project1, "Project #1 -updated", "Project #1 description -updated", "Department #1 -updated", user2, user1);

        workSpace.addMemberToProject(user1);
        workSpace.addMemberToProject(user2);
        workSpace.addMemberToProject(user3);
        workSpace.removeMemberFromProject(user3);

        // 2) Я как пользователь хочу заводить разные типы задач в backlog:
        Issue issue1 = workSpace.createIssueInBacklog(IssueType.EPIC, "Main Issue", null, null, null, IssuePriority.HIGH, LocalDate.of(2012, 11, 1), user1, null, null);

        workSpace.updateIssue(issue1, IssueType.EPIC, "Issue #1", null, null, null, IssuePriority.MEDIUM, LocalDate.of(2012, 12, 1), user1, null, null);

        Issue issue2 = workSpace.createIssueInBacklog(IssueType.BUG, "Issue #2", null, null, null, IssuePriority.HIGH, null, user1, null, null);
        Issue issue3 = workSpace.createIssueInBacklog(IssueType.STORY, "Issue #3", null, null, null, IssuePriority.LOW, LocalDate.of(2012, 11, 1), user2, null, null);
        Issue issue4 = workSpace.createIssueInBacklog(IssueType.TASK, "Issue #4", null, null, null, IssuePriority.LOW, null, user1, null, null);
        Issue issue5 = workSpace.createIssueInBacklog(IssueType.STORY, "Issue #5", null, null, null, IssuePriority.LOW, null, user2, null, null);
        Issue issue6 = workSpace.createIssueInBacklog(IssueType.TASK, "Issue #6", null, null, null, IssuePriority.HIGH, null, user1, null, null);

        workSpace.showFilter(project1.getProjectBacklog().getIssueList(), "Issues" + project1.getProjectBacklog().getTitle() + ":");

        // 3) Я как пользователь хочу декомпозировать задачи с привязкой к родительской задаче и переносить задачи из backlog в sprint.
        ProjectSprint projectSprint1 = workSpace.createProjectSprint("Sprint #1", LocalDate.of(2011,11,1), LocalDate.of(2011,11,1));
        workSpace.updateSprint(projectSprint1, "new Sprint #1", LocalDate.of(2012,11,1), LocalDate.of(2012,12,1));

        Issue issue7 = workSpace.createChildIssue(issue2, "Sub issue of Issue #2" + issue2.getTitle());
        Issue issue8 = workSpace.createChildIssue(issue3, "Sub issue of Issue #3 " + issue3.getTitle());

        workSpace.moveIssueToSprint(issue2);
        workSpace.moveIssueToSprint(issue3);
        workSpace.moveIssueToSprint(issue7);
        workSpace.moveIssueToSprint(issue8);

        workSpace.deleteIssueFromProject(issue6);
        workSpace.deleteIssueFromProject(issue4);

        workSpace.showFilter(project1.getProjectBacklog().getIssueList(), "Issues " + project1.getProjectBacklog().getTitle() + ":");
        workSpace.showFilter(project1.getCurrentSprint().getIssueList(), "Issues " + project1.getCurrentSprint().getTitle() + ":");

        // 4) Я как пользователь хочу гибко настраивать фильтры задач в backlog:
        request = new Request();
        request.add(FilterType.PRIORITY, new IssuePriority[]{IssuePriority.HIGH, IssuePriority.LOW});
        request.add(FilterType.CREATION_AFTER, new LocalDate[]{LocalDate.of(2008, 11, 1), LocalDate.of(2005, 11, 1)});
        request.add(FilterType.CREATION_BEFORE, new LocalDate[]{LocalDate.of(2015, 11, 1)});
        request.add(FilterType.EXECUTOR, new User[]{user1, user2});

        List<Issue> pool = chain.startFilter(request, project1.getProjectBacklog().getIssueList());
        workSpace.showFilter(pool, "The filter chain of " + project1.getProjectBacklog().getTitle() + ":");

        request.remove(FilterType.CREATION_AFTER);
        pool = chain.startFilter(request, project1.getCurrentSprint().getIssueList());
        workSpace.showFilter(pool, "The filter chain of " + project1.getCurrentSprint().getTitle() + ":");

        //5) Я как пользователь хочу управлять жизненным циклом конкретной задачи и гибко настраивать workflow:
        WorkFlow newWorkFlow = workSpace.createWorkflow("Workflow #1");
        workSpace.addStatusToWorkFlow(newWorkFlow, WorkFlowStatus.OPEN_ISSUE);
        workSpace.addStatusToWorkFlow(newWorkFlow, WorkFlowStatus.CLOSE_ISSUE);
        workSpace.addStatusToWorkFlow(newWorkFlow, WorkFlowStatus.REVIEW_ISSUE);
        workSpace.removeStatusFromWorkFlow(newWorkFlow, WorkFlowStatus.REVIEW_ISSUE);

        Issue issue9 = workSpace.createIssueInBacklog(IssueType.EPIC, "Issue #9", null, null, newWorkFlow, IssuePriority.HIGH, LocalDate.of(2012, 11, 1), user1, null, WorkFlowStatus.OPEN_ISSUE);
        issue9.setNextStatus();
        issue9.setNextStatus();

        workSpace.showFilter(project1.getProjectBacklog().getIssueList(), "Issues " + project1.getProjectBacklog().getTitle() + ":");

        int projectId = workSpace.getCurrentProject().getId();

        System.out.println("Delete data");
        workSpace.setCurrentProject(null);
        System.out.println("current project - " + workSpace.getCurrentProject());

        System.out.println("Restoring from the database");
        workSpace.setCurrentProject(workSpace.loadProject(projectId));

        System.out.println("current project - " + workSpace.getCurrentProject());
        workSpace.showFilter(project1.getProjectBacklog().getIssueList(), "Issues " + project1.getProjectBacklog().getTitle() + ":");
        workSpace.showFilter(project1.getCurrentSprint().getIssueList(), "Issues " + project1.getCurrentSprint().getTitle() + ":");
    }
}