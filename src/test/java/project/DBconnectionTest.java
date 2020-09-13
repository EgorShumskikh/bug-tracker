package project;

import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.junit.*;
import project.model.backlog.ProjectSprint;
import project.model.factory.MainFactory;
import project.controller.SingleController;
import project.model.issue.Issue;
import project.model.issue.IssuePriority;
import project.model.issue.IssueType;
import project.model.project.ScrumProject;
import project.model.user.User;
import project.model.workflow.WorkFlow;
import project.model.workflow.WorkFlowStatus;
import org.junit.Test;
import java.io.File;
import java.net.MalformedURLException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Map;

import static org.dbunit.Assertion.assertEquals;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;

public class DBconnectionTest {

    private static IDataSet loadedDataSet;
    private static MainFactory mainFactory;
    private IDatabaseConnection iDatabaseConnection;
    private Connection conn;

    @BeforeClass
    public static void setUpBeforeClass() throws MalformedURLException, DataSetException {
        loadedDataSet = new FlatXmlDataSetBuilder().build(new File("src/test/resources/dbunitData.xml"));
        mainFactory = SingleController.getInstance();
    }

    @Before
    public void setUp() throws DatabaseUnitException, SQLException {
        conn = mainFactory.getBdConnect().getConnection();
        mainFactory.getBdConnect().executeSqlScript(conn, new File("src/test/resources/Project_drop.sql"));
        mainFactory.getBdConnect().executeSqlScript(conn, new File("src/test/resources/Project_create.sql"));
        iDatabaseConnection = new DatabaseConnection(conn);
    }

    @After
    public void tearDown() throws SQLException {
        iDatabaseConnection.close();
        conn.close();
    }

    @Test
    public void test_1_createUser_in_DB() throws Exception {
        mainFactory.createUser("Иван", "Иванов", "Иванович");

        ITable actualTable = iDatabaseConnection.createTable("users");
        ITable expectedTable = loadedDataSet.getTable("users_create");

        assertEquals(expectedTable, actualTable);
    }

    @Test
    public void test_2_updateUser_in_DB() throws Exception {
        User user = mainFactory.createUser("Иван", "Иванов", "Петрович");
        mainFactory.updateUser(user, "Коба", "", "Иванович");

        ITable actualTable = iDatabaseConnection.createTable("users");
        ITable expectedTable = loadedDataSet.getTable("users_update");

        assertEquals(expectedTable, actualTable);
    }

    @Test
    public void test_3_addMemberToProject_in_DB() throws Exception {
        User user1 = mainFactory.createUser("Коба", null, "Иванович");
        User user2 = mainFactory.createUser("Вениамин", null, null);
        mainFactory.createProject("Проект 1", "Описание проекта 1", "Подразделение 1", null, null);
        mainFactory.addMemberToProject(user1);
        mainFactory.addMemberToProject(user2);

        ITable actualTable = iDatabaseConnection.createTable("project_members");
        ITable expectedTable = loadedDataSet.getTable("project_members_create");

        assertEquals(expectedTable, actualTable);
    }

    @Test
    public void test_4_removeMemberFromProject_in_DB() throws Exception {
        User user1 = mainFactory.createUser("Коба", null, "Иванович");
        User user2 = mainFactory.createUser("Вениамин", null, null);
        mainFactory.createProject("Проект 1", "Описание проекта 1", "Подразделение 1", null, null);
        mainFactory.addMemberToProject(user1);
        mainFactory.addMemberToProject(user2);
        mainFactory.removeMemberFromProject(user2);

        ITable actualTable = iDatabaseConnection.createTable("project_members");
        ITable expectedTable = loadedDataSet.getTable("project_members_remove");

        assertEquals(expectedTable, actualTable);
    }

    @Test
    public void test_5_createProgect_in_DB() throws Exception {
        mainFactory.createProject("Проект 1", "Описание проекта 1", "Подразделение 1", null, null);

        ITable actualTable = iDatabaseConnection.createTable("projects");
        ITable expectedTable = loadedDataSet.getTable("projects_create");

        assertEquals(expectedTable, actualTable);
    }

    @Test
    public void test_6_updateProgect_in_DB() throws Exception {
        ScrumProject project = mainFactory.createProject("Проект 1", "Описание проекта 1", "Подразделение 1", null, null);
        User user1 = mainFactory.createUser("Коба", null, "Иванович");
        User user2 = mainFactory.createUser("Вениамин", null, null);
        mainFactory.updateProgect(project, "Обновленный Проект 1", "Обновленное Описание проекта 1", "Обновленное Подразделение 1", user1, user2);

        ITable actualTable = iDatabaseConnection.createTable("projects");
        ITable expectedTable = loadedDataSet.getTable("projects_update");

        assertEquals(expectedTable, actualTable);
    }

    @Test
    public void test_7_createSprintInProject_in_DB_sprints() throws Exception {
        mainFactory.createProject("Проект 1", "Описание проекта 1", "Подразделение 1", null, null);
        mainFactory.createProjectSprint("sprint1", LocalDate.of(2020, 6, 1), LocalDate.of(2020, 7, 1));

        ITable actualTable = iDatabaseConnection.createTable("sprints");
        ITable expectedTable = loadedDataSet.getTable("sprints_create");

        assertEquals(expectedTable, actualTable);
    }

    @Test
    public void test_8_create_createSprintInProject_in_DB_projects() throws Exception {
        mainFactory.createProject("Проект 1", "Описание проекта 1", "Подразделение 1", null, null);
        mainFactory.createProjectSprint("sprint1", LocalDate.of(2020, 6, 1), LocalDate.of(2020, 7, 1));

        ITable actualTable = iDatabaseConnection.createTable("projects");
        ITable expectedTable = loadedDataSet.getTable("projects_create_sprint");

        assertEquals(expectedTable, actualTable);
    }

    @Test
    public void test_9_updateSprint_in_DB() throws Exception {
        mainFactory.createProject("Проект 1", "Описание проекта 1", "Подразделение 1", null, null);
        ProjectSprint sprint = mainFactory.createProjectSprint("sprint1", LocalDate.of(2020, 6, 1), LocalDate.of(2020, 7, 1));
        mainFactory.updateSprint(sprint, "sprint1-new", LocalDate.of(2022, 6, 1), LocalDate.of(2022, 7, 3));

        ITable actualTable = iDatabaseConnection.createTable("sprints");
        ITable expectedTable = loadedDataSet.getTable("sprints_update");

        assertEquals(expectedTable, actualTable);
    }

    @Test
    public void test_10_createIssueInBacklog_in_DB_issues() throws Exception {
        mainFactory.createProject("Проект 1", "Описание проекта 1", "Подразделение 1", null, null);
        User user1 = mainFactory.createUser(null, "Иванов", null);
        mainFactory.createIssueInBacklog(IssueType.EPIC, "Главная задача", null, "Описание", null, IssuePriority.HIGH, LocalDate.of(2012, 11, 1), user1, null, WorkFlowStatus.OPEN_ISSUE);

        ITable actualTable = iDatabaseConnection.createTable("issues");
        ITable expectedTable = loadedDataSet.getTable("issues_create");

        assertEquals(expectedTable, actualTable);
    }

    @Test
    public void test_11_updateIssue_in_DB() throws Exception {
        mainFactory.createProject("Проект 1", "Описание проекта 1", "Подразделение 1", null, null);
        User user1 = mainFactory.createUser(null, "Иванов", null);
        Issue issue1 = mainFactory.createIssueInBacklog(IssueType.EPIC, "Главная задача", null, "Описание", null, IssuePriority.HIGH, LocalDate.of(2012, 11, 1), user1, null, WorkFlowStatus.OPEN_ISSUE);
        mainFactory.updateIssue(issue1, IssueType.EPIC, "Второстепенная задача", null, "Описание новое", null, IssuePriority.HIGH, LocalDate.of(2020, 11, 1), user1, null, WorkFlowStatus.CLOSE_ISSUE);

        ITable actualTable = iDatabaseConnection.createTable("issues");
        ITable expectedTable = loadedDataSet.getTable("issues_update");

        assertEquals(expectedTable, actualTable);
    }

    @Test
    public void test_11_createChildIssue_in_DB() throws Exception {
        mainFactory.createProject("Проект 1", "Описание проекта 1", "Подразделение 1", null, null);
        User user1 = mainFactory.createUser(null, "Иванов", null);
        Issue issue1 = mainFactory.createIssueInBacklog(IssueType.EPIC, "Главная задача", null, "Описание", null, IssuePriority.HIGH, LocalDate.of(2012, 11, 1), user1, null, WorkFlowStatus.OPEN_ISSUE);
        mainFactory.createChildIssue(issue1, "child");

        ITable actualTable = iDatabaseConnection.createTable("issues");
        ITable expectedTable = loadedDataSet.getTable("issues_child");

        assertEquals(expectedTable, actualTable);
    }

    @Test
    public void test_12_deleteIssueFromProject_in_DB() throws Exception {
        mainFactory.createProject("Проект 1", "Описание проекта 1", "Подразделение 1", null, null);
        User user1 = mainFactory.createUser(null, "Иванов", null);
        Issue issue1 = mainFactory.createIssueInBacklog(IssueType.EPIC, "Задача1", null, "Описание", null, IssuePriority.HIGH, LocalDate.of(2012, 11, 1), user1, null, WorkFlowStatus.OPEN_ISSUE);
        Issue issue2 = mainFactory.createIssueInBacklog(IssueType.EPIC, "Задача2", null, "Описание", null, IssuePriority.HIGH, LocalDate.of(2012, 11, 1), user1, null, WorkFlowStatus.OPEN_ISSUE);
        Issue issue3 = mainFactory.createIssueInBacklog(IssueType.EPIC, "Задача3", null, "Описание", null, IssuePriority.HIGH, LocalDate.of(2012, 11, 1), user1, null, WorkFlowStatus.OPEN_ISSUE);
        mainFactory.deleteIssueFromProject(issue2);

        ITable actualTable = iDatabaseConnection.createTable("issues");
        ITable expectedTable = loadedDataSet.getTable("issues_delete");

        assertEquals(expectedTable, actualTable);
    }

    @Test
    public void test_13_moveIssueToSprint_in_DB() throws Exception {
        mainFactory.createProject("Проект 1", "Описание проекта 1", "Подразделение 1", null, null);
        User user1 = mainFactory.createUser(null, "Иванов", null);
        Issue issue1 = mainFactory.createIssueInBacklog(IssueType.EPIC, "Задача1", null, "Описание", null, IssuePriority.HIGH, LocalDate.of(2012, 11, 1), user1, null, WorkFlowStatus.OPEN_ISSUE);
        mainFactory.createProjectSprint("sprint1", LocalDate.of(2020, 1, 2), LocalDate.of(2020, 3, 4));
        mainFactory.moveIssueToSprint(issue1);

        ITable actualTable = iDatabaseConnection.createTable("issues");
        ITable expectedTable = loadedDataSet.getTable("issues_to_sprint");

        assertEquals(expectedTable, actualTable);
    }

    @Test
    public void test_14_moveIssueToBacklog_in_DB() throws Exception {
        mainFactory.createProject("Проект 1", "Описание проекта 1", "Подразделение 1", null, null);
        User user1 = mainFactory.createUser(null, "Иванов", null);
        Issue issue1 = mainFactory.createIssueInBacklog(IssueType.EPIC, "Задача1", null, "Описание", null, IssuePriority.HIGH, LocalDate.of(2012, 11, 1), user1, null, WorkFlowStatus.OPEN_ISSUE);
        mainFactory.createProjectSprint("sprint1", LocalDate.of(2020, 1, 2), LocalDate.of(2020, 3, 4));
        mainFactory.moveIssueToSprint(issue1);
        mainFactory.moveIssueToBacklog(issue1);

        ITable actualTable = iDatabaseConnection.createTable("issues");
        ITable expectedTable = loadedDataSet.getTable("issues_to_backlog");

        assertEquals(expectedTable, actualTable);
    }


    @Test
    public void test_15_createWorkflow_in_DB() throws Exception {
        mainFactory.createWorkflow("Workflow1");
        mainFactory.createWorkflow("Workflow2");
        mainFactory.createWorkflow("Workflow3");

        ITable actualTable = iDatabaseConnection.createTable("workflows");
        ITable expectedTable = loadedDataSet.getTable("workflows_create");

        assertEquals(expectedTable, actualTable);
    }

    @Test
    public void test_16_addStatusToWorkFlow_in_DB() throws Exception {
        WorkFlow workFlow = mainFactory.createWorkflow("Workflow1");
        mainFactory.addStatusToWorkFlow(workFlow, WorkFlowStatus.OPEN_ISSUE);
        mainFactory.addStatusToWorkFlow(workFlow, WorkFlowStatus.CLOSE_ISSUE);

        ITable actualTable = iDatabaseConnection.createTable("workflow_statuses");
        ITable expectedTable = loadedDataSet.getTable("workflow_statuses_add");

        assertEquals(expectedTable, actualTable);
    }

    @Test
    public void test_17_removeStatusToWorkFlow_in_DB() throws Exception {
        WorkFlow workFlow = mainFactory.createWorkflow("Workflow1");
        mainFactory.addStatusToWorkFlow(workFlow, WorkFlowStatus.OPEN_ISSUE);
        mainFactory.addStatusToWorkFlow(workFlow, WorkFlowStatus.INPROGRESS_ISSUE);
        mainFactory.addStatusToWorkFlow(workFlow, WorkFlowStatus.TEST_ISSUE);
        mainFactory.addStatusToWorkFlow(workFlow, WorkFlowStatus.CLOSE_ISSUE);
        mainFactory.removeStatusFromWorkFlow(workFlow, WorkFlowStatus.CLOSE_ISSUE);

        ITable actualTable = iDatabaseConnection.createTable("workflow_statuses");
        ITable expectedTable = loadedDataSet.getTable("workflow_statuses_remove");

        assertEquals(expectedTable, actualTable);
    }

    @Test
    public void test_18_getUsers_from_base()  {
        mainFactory.createUser("Иван", "Иванов", "Иванович");
        mainFactory.createUser("Максим", "Петров", "Петрович");
        mainFactory.createUser("Джон", "Сидоров", "Альбертович");

        Map<Integer, String> users = mainFactory.getUsers();

        assertThat(users, hasEntry(1, "Иванов Иван Иванович"));
        assertThat(users, hasEntry(2, "Петров Максим Петрович"));
        assertThat(users, hasEntry(3, "Сидоров Джон Альбертович"));
    }

    @Test
    public void test_19_getWorkflows_from_base()  {
        mainFactory.createWorkflow("Workflow1");
        mainFactory.createWorkflow("Workflow2");
        mainFactory.createWorkflow("Workflow3");

        Map<Integer, String> workflows = mainFactory.getWorkflows();

        assertThat(workflows, hasEntry(1, "Workflow1"));
        assertThat(workflows, hasEntry(2, "Workflow2"));
        assertThat(workflows, hasEntry(3, "Workflow3"));
    }

    @Test
    public void test_20_geProjects_from_base()  {
        mainFactory.createProject("Проект 1", "Описание проекта 1", "Подразделение 1", null, null);
        mainFactory.createProject("Проект 2", "Описание проекта 1", "Подразделение 1", null, null);
        mainFactory.createProject("Проект 3", "Описание проекта 1", "Подразделение 1", null, null);

        Map<Integer, String> projects = mainFactory.getProjects();

        assertThat(projects, hasEntry(1, "Проект 1"));
        assertThat(projects, hasEntry(2, "Проект 2"));
        assertThat(projects, hasEntry(3, "Проект 3"));
    }

    @Test
    public void test_21_loadProject_from_base_test_and_comparing_to_original() {
        User user1 = mainFactory.createUser("Коба", "", "Иванович");
        User user2 = mainFactory.createUser(null, "Петров", null);
        User user3 = mainFactory.createUser(null, "Сидоров", null);

        ScrumProject project1 = mainFactory.createProject("Проект №1", "Описание проекта №1", "Подразделение №1", user1, null);
        mainFactory.updateProgect(project1, "Проект №1-обновлен", "Описание проекта №1-обновлено", "Подразделение №1-новое", user2, user1);

        mainFactory.addMemberToProject(user1);
        mainFactory.addMemberToProject(user2);
        mainFactory.addMemberToProject(user3);
        mainFactory.removeMemberFromProject(user3);

        Issue issue1 = mainFactory.createIssueInBacklog(IssueType.EPIC, "Главная задача", null, null, null, IssuePriority.HIGH, LocalDate.of(2012, 11, 1), user1, null, null);
        mainFactory.updateIssue(issue1, IssueType.EPIC, "Уже не Главная задача", null, null, null, IssuePriority.MEDIUM, LocalDate.of(2012, 12, 1), user1, null, null);

        Issue issue2 = mainFactory.createIssueInBacklog(IssueType.BUG, "Задача 1", null, null, null, IssuePriority.HIGH, null, user1, null, null);
        Issue issue3 = mainFactory.createIssueInBacklog(IssueType.STORY, "Задача 2", null, null, null, IssuePriority.LOW, LocalDate.of(2012, 11, 1), user2, null, null);
        Issue issue4 = mainFactory.createIssueInBacklog(IssueType.TASK, "Задача 3", null, null, null, IssuePriority.LOW, null, user1, null, null);
        Issue issue5 = mainFactory.createIssueInBacklog(IssueType.STORY, "Задача 4", null, null, null, IssuePriority.LOW, null, user2, null, null);
        Issue issue6 = mainFactory.createIssueInBacklog(IssueType.TASK, "Задача 5", null, null, null, IssuePriority.HIGH, null, user1, null, null);

        ProjectSprint projectSprint1 = mainFactory.createProjectSprint("Sprint 1", LocalDate.of(2011, 11, 1), LocalDate.of(2011, 11, 1));
        mainFactory.updateSprint(projectSprint1, "new Sprint 1", LocalDate.of(2012, 11, 1), LocalDate.of(2012, 12, 1));
        mainFactory.moveIssueToSprint(issue2);
        mainFactory.moveIssueToSprint(issue3);

        Issue issue7 = mainFactory.createChildIssue(issue2, "Подзадача " + issue2.getTitle());
        Issue issue8 = mainFactory.createChildIssue(issue3, "Подзадача " + issue3.getTitle());

        ProjectSprint projectSprint2 = mainFactory.createProjectSprint("Sprint 2", LocalDate.of(2011, 11, 1), LocalDate.of(2011, 11, 1));
        mainFactory.moveIssueToSprint(issue7);
        mainFactory.moveIssueToSprint(issue8);

        mainFactory.deleteIssueFromProject(issue6);
        mainFactory.deleteIssueFromProject(issue4);

        WorkFlow newWorkFlow = mainFactory.createWorkflow("Workflow1");
        mainFactory.addStatusToWorkFlow(newWorkFlow, WorkFlowStatus.OPEN_ISSUE);
        mainFactory.addStatusToWorkFlow(newWorkFlow, WorkFlowStatus.CLOSE_ISSUE);
        mainFactory.addStatusToWorkFlow(newWorkFlow, WorkFlowStatus.REVIEW_ISSUE);
        mainFactory.removeStatusFromWorkFlow(newWorkFlow, WorkFlowStatus.REVIEW_ISSUE);

        Issue issue9 = mainFactory.createIssueInBacklog(IssueType.EPIC, "Задача9", null, null, newWorkFlow, IssuePriority.HIGH, LocalDate.of(2012, 11, 1), user1, null, WorkFlowStatus.OPEN_ISSUE);
        mainFactory.setNextIssueStatus(issue9);
        mainFactory.setNextIssueStatus(issue9);

        System.out.println("Текущий проект - " + project1);
        mainFactory.showFilter(project1.getProjectBacklog().getIssueList(), "Задачи " + project1.getProjectBacklog().getTitle() + ":");
        project1.getSprints().forEach(l -> mainFactory.showFilter(l.getIssueList(), "Задачи " + l.getTitle() + ":"));

        ScrumProject project2 = mainFactory.loadProject(project1.getId());
        System.out.println("Восстановленный из базы проект - " + project2);
        mainFactory.showFilter(project2.getProjectBacklog().getIssueList(), "Задачи " + project2.getProjectBacklog().getTitle() + ":");
        project2.getSprints().forEach(l -> mainFactory.showFilter(l.getIssueList(), "Задачи " + l.getTitle() + ":"));

        assertEquals(project1, project2);
    }
}
