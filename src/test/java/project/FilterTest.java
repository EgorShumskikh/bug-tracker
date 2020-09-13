package project;

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
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class FilterTest {

    private MainFactory mainFactory;
    private Request request;
    private List<Issue> pool;
    private ChainOfFilters chain;
    private ScrumProject project;
    private User user1;
    private User user2;
    private User user3;
    private Issue issue1, issue2, issue3, issue4, issue5, issue6, issue7, issue8, issue9;

    @Before
    public void setUp() {

        mainFactory = SingleController.getInstance();
        request = new Request();
        chain = new ChainOfFilters();

        user1 = mainFactory.createUser( null, "Иванов", null);
        user2 = mainFactory.createUser(null, "Петров", null);
        user3 = mainFactory.createUser( null, "Сидоров", null);

        project = mainFactory.createProject("Проект №1", "Описание проекта №1", "Подразделение №1", user1, user2);

        issue1 = mainFactory.createIssueInBacklog(IssueType.EPIC, "Главная задача",  null, null, null,IssuePriority.HIGH, null,user1, null, null);
        issue2 = mainFactory.createIssueInBacklog(IssueType.BUG, "Задача 1",  null,null, null,IssuePriority.HIGH, null,user1, null, null);
        issue3 = mainFactory.createIssueInBacklog(IssueType.STORY, "Задача 2", null, null, null,IssuePriority.LOW, null,user2, null, null);
        issue4 = mainFactory.createIssueInBacklog(IssueType.TASK, "Задача 3",  null,null, null,IssuePriority.LOW, null,user1, null, null);
        issue5 = mainFactory.createIssueInBacklog(IssueType.STORY, "Задача 4", null,null, null, IssuePriority.MEDIUM, null,user1, null, null);
        issue6 = mainFactory.createIssueInBacklog(IssueType.TASK, "Задача 5",  null,null, null,IssuePriority.HIGH, null,user1, null, null);
        issue7 = mainFactory.createChildIssue(issue2, "Подзадача " + issue2.getTitle());
        issue8 = mainFactory.createChildIssue(issue3, "Подзадача " + issue3.getTitle());
        issue9 = mainFactory.createIssueInBacklog(IssueType.EPIC, "Задача 8", null,null, null, IssuePriority.MEDIUM, null,user3, null, null);
        issue1.setCreationDate(LocalDate.of(2010, 11, 1));
        issue3.setCreationDate(LocalDate.of(2012, 11, 1));

        mainFactory.createProjectSprint("Sprint 1", LocalDate.of(2011,11,1), LocalDate.of(2011,11,1));

        project.moveIssueToSprint(issue2);
        project.moveIssueToSprint(issue3);
        project.moveIssueToSprint(issue7);
        project.moveIssueToSprint(issue8);

        issue1.setReporter(user1);
        issue2.setReporter(user2);
        issue3.setReporter(user3);
        issue4.setReporter(user3);
    }

    @Test
    public void tes_FilterTest_priority_in_backlog() {
        request.add(FilterType.PRIORITY, new IssuePriority[]{IssuePriority.HIGH, IssuePriority.MEDIUM});
        pool = chain.startFilter(request, project.getProjectBacklog().getIssueList());

        assertThat(pool, containsInAnyOrder(issue1, issue5, issue6, issue9));
    }

    @Test
    public void tes_FilterTest_title_in_backlog() {
        request.add(FilterType.TITLE, new String[]{"Главная задача", "Задача 3", "Задача 4"});
        pool = chain.startFilter(request, project.getProjectBacklog().getIssueList());

        assertThat(pool, containsInAnyOrder(issue1, issue4, issue5));
    }

    @Test
    public void tes_FilterTest_executor_in_backlog() {
        request.add(FilterType.EXECUTOR, new User[]{user1, user3});
        pool = chain.startFilter(request, project.getProjectBacklog().getIssueList());

        assertThat(pool, containsInAnyOrder(issue1, issue4, issue5, issue6, issue9));
    }

    @Test
    public void tes_FilterTest_reporter_in_backlog() {
        request.add(FilterType.REPORTER, new User[]{user1, user3});
        pool = chain.startFilter(request, project.getProjectBacklog().getIssueList());

        mainFactory.showFilter(pool, "Цепочка фильтров из " + project.getProjectBacklog().getTitle() + ":");

        assertThat(pool, containsInAnyOrder(issue1, issue4));
    }


    @Test
    public void tes_FilterTest_priority_and_creationDate_and_executor_in_backlog() {
        request.add(FilterType.PRIORITY, new IssuePriority[]{IssuePriority.HIGH, IssuePriority.LOW});
        request.add(FilterType.CREATION_AFTER, new LocalDate[]{LocalDate.of(2008, 11, 1), LocalDate.of(2005, 11, 1)});
        request.add(FilterType.CREATION_BEFORE, new LocalDate[]{LocalDate.of(2015, 11, 1)});
        request.add(FilterType.EXECUTOR, new User[]{user1, user2});

        pool = chain.startFilter(request, project.getProjectBacklog().getIssueList());

        assertThat(pool, containsInAnyOrder(issue1));
    }

    @Test
    public void tes_FilterTest_priority_and_creationDate_and_executor_in_sprint() {
        request.add(FilterType.PRIORITY, new IssuePriority[]{IssuePriority.HIGH, IssuePriority.LOW});
        request.add(FilterType.CREATION_AFTER, new LocalDate[]{LocalDate.of(2008, 11, 1), LocalDate.of(2005, 11, 1)});
        request.add(FilterType.CREATION_BEFORE, new LocalDate[]{LocalDate.of(2015, 11, 1)});
        request.add(FilterType.EXECUTOR, new User[]{user1, user2});

        pool = chain.startFilter(request, project.getCurrentSprint().getIssueList());

        assertThat(pool, containsInAnyOrder(issue3));
    }

    @Test
    public void tes_FilterTest_priority_and_creationDate_and_executor_in_backlog_and_sprint() {
        request.add(FilterType.PRIORITY, new IssuePriority[]{IssuePriority.HIGH, IssuePriority.LOW});
        request.add(FilterType.CREATION_AFTER, new LocalDate[]{LocalDate.of(2008, 11, 1), LocalDate.of(2005, 11, 1)});
        request.add(FilterType.CREATION_BEFORE, new LocalDate[]{LocalDate.of(2015, 11, 1)});
        request.add(FilterType.EXECUTOR, new User[]{user1, user2});

        List<Issue> newList = project.getCurrentSprint().getIssueList();
        newList.addAll(project.getProjectBacklog().getIssueList());

        pool = chain.startFilter(request, newList);
        assertThat(pool, containsInAnyOrder(issue1, issue3));
    }
}
