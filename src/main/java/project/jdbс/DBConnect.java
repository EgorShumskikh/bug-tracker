package project.jdbс;

import project.model.backlog.ProjectBacklog;
import project.model.backlog.ProjectSprint;
import project.model.factory.backlog.BacklogFactoryImpl;
import project.model.factory.issue.IssueFactory;
import project.model.factory.issue.IssueFactoryImpl;
import project.model.factory.project.ProjectFactoryImpl;
import project.model.factory.sprint.SprintFactoryImpl;
import project.model.factory.user.UserFactoryImpl;
import project.model.factory.user.UserFactory;
import project.model.issue.Issue;
import project.model.issue.IssuePriority;
import project.model.issue.IssueType;
import project.model.project.Project;
import project.model.project.ScrumProject;
import project.model.user.User;
import project.model.workflow.WorkFlow;
import project.model.workflow.WorkFlowStatus;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.*;
import java.sql.Date;
import java.util.*;

public class DBConnect {

    private static final String DB_URL = "jdbc:postgresql://localhost:5432/Project";
    private static final String USER = "postgres";
    private static final String PASS = "";
    private static final String INSERT_INTO_PROJECTS = "insert into projects values(nextval('projects_id_seq'), ?, ?, ?, ?, ?, ?)";
    private static final String INSERT_INTO_USERS = "insert into users values(nextval('users_id_seq'), ?, ?, ?)";
    private static final String INSERT_INTO_PROJECT_MEMBERS = "insert into project_members values(nextval('project_members_id_seq'), ?, ?)";
    private static final String INSERT_INTO_SPRINTS = "insert into sprints values(nextval('sprints_id_seq'), ?, ?, ?, ?)";
    private static final String INSERT_INTO_BACKLOGS = "insert into backlogs values(nextval('backlogs_id_seq'), ?, ?)";
    private static final String INSERT_INTO_ISSUES = "insert into issues values(nextval('issues_id_seq'), ?, ?, ?, ? , ?, ?, ?, ? , ?, ?, ?, ?)";
    private static final String INSERT_INTO_WORKFLOWS = "insert into workflows values(nextval('workflows_id_seq'), ?)";
    private static final String INSERT_INTO_WORKFLOW_STATUSES = "insert into workflow_statuses values(nextval('workflow_statuses_id_seq'), ?,? )";
    private static final String UPDATE_ISSUE_MOVE_TO_SPRINT = "update issues set backlog_id = null, sprint_id = ? where id = ?";
    private static final String UPDATE_ISSUE_MOVE_TO_BACKLOG = "update issues set backlog_id = ?, sprint_id = null where id = ?";
    private static final String UPDATE_PROJECT_SET_CURRENTSPRINT = "update projects set current_sprint_id = ? where id = ?";
    private static final String UPDATE_ISSUE_SET_ALL_ATTRIBUTES = "update issues set issue_type = ?, title =?, parent_issue_id = ?, description = ?," +
            "workflow_id=?, issue_priority=?, creation_date=?, executor_id=?, reporter_id=?, current_status_of_workflow=? where id = ?";
    private static final String UPDATE_PROJECT_SET_ALL_ATTRIBUTES = "update projects set title =?, description = ?, department_name = ?," +
            "supervisor_id=?, admin_id=?, current_sprint_id=? where id = ?";
    private static final String UPDATE_CURRENT_SPRINT = "update projects set current_sprint_id=? where id = ?";
    private static final String UPDATE_USER_SET_ALL_ATTRIBUTES = "update users set first_name =?, last_name = ?, patronymic = ? where id = ?";
    private static final String UPDATE_SPRINT_SET_ALL_ATTRIBUTES = "update sprints set title =?, start_date = ?, finish_date = ? where id = ?";
    private static final String DELETE_FROM_PROJECT_MEMBERS = "delete from project_members where users_id = ? and projects_id = ?";
    private static final String DELETE_FROM_WORKFLOW_STATUSES = "delete from workflow_statuses where workflow_id = ? and status_name = ?";
    private static final String DELETE_ISSUE_FROM_PROJECT = "delete from issues where id = ?";
    private static final String SELECT_PROJECTS_LIST = "select * from projects";
    private static final String SELECT_USERS_LIST = "select * from users";
    private static final String SELECT_WORKFLOWS_LIST = "select * from workflows";
    private static final String SELECT_PARENT_ISSUES = "select * from issues where parent_issue_id>0";
    private static final String SELECT_USER_BY_ID = "select * from users where id = ?";
    private static final String SELECT_WORKFLOW_BY_ID = "select * from workflows a  left join workflow_statuses b on a.id = b.workflow_id where a.id = ?";
    private static final String SELECT_ISSUE_BY_ID = "select * from issues where id = ?";
    private static final String SELECT_BACKLOG_BY_PROJECT_ID = "select * from backlogs a left join issues b on a.id=b.backlog_id  where project_id = ?";
    private static final String SELECT_SPRINTS_BY_PROJECT_ID = "select * from sprints where project_id = ?";
    private static final String SELECT_SPRINT_BY_SPRINT_ID = "select * from sprints a left join issues b on a.id=b.sprint_id  where a.id = ?";
    private static final String SELECT_PROJECT_BY_ID = "select * from projects where id = ?";
    private static final String SELECT_MEMBERS_BY_PROJECT_ID = "select * from project_members where projects_id = ?";

    public Connection getConnection() throws SQLException {
        Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
        return conn;
    }

    public void updateUser(User user) {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_USER_SET_ALL_ATTRIBUTES)) {
            stmt.setString(1, user.getFirstName());
            stmt.setString(2, user.getLastName());
            stmt.setString(3, user.getPatronymic());
            stmt.setInt(4, user.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isparent(Issue issue) {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_PARENT_ISSUES, Statement.RETURN_GENERATED_KEYS);
        ) {

            ResultSet rs = stmt.executeQuery();
            while (rs.next())
                if (issue.getId() == rs.getInt(4))
                    return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void updateSprint(ProjectSprint sprint) {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_SPRINT_SET_ALL_ATTRIBUTES)) {
            stmt.setString(1, sprint.getTitle());
            stmt.setDate(2, sprint.getStartDate() == null ? null : Date.valueOf(sprint.getStartDate()));
            stmt.setDate(3, sprint.getFinishDate() == null ? null : Date.valueOf(sprint.getFinishDate()));
            stmt.setInt(4, sprint.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateProject(Project project) {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_PROJECT_SET_ALL_ATTRIBUTES)) {
            stmt.setString(1, project.getTitle());
            stmt.setString(2, project.getDescription());
            stmt.setString(3, project.getDepartmentName());
            stmt.setObject(4, project.getSupervisor() == null ? null : project.getSupervisor().getId());
            stmt.setObject(5, project.getAdmin() == null ? null : project.getAdmin().getId());
            stmt.setObject(6, null);
            stmt.setInt(7, project.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateCurrentSprint(ScrumProject project, ProjectSprint sprint) {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_CURRENT_SPRINT)) {
            stmt.setInt(1, sprint.getId());
            stmt.setInt(2, project.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateIssue(Issue issue) {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_ISSUE_SET_ALL_ATTRIBUTES)) {
            stmt.setString(1, String.valueOf(issue.getIssueType()));
            stmt.setString(2, issue.getTitle());
            stmt.setObject(3, issue.getParentIsue() == null ? null : issue.getParentIsue().getId());
            stmt.setString(4, issue.getDescription());
            stmt.setObject(5, issue.getWorkFlow() == null ? null : issue.getWorkFlow().getId());
            stmt.setString(6, issue.getPriority().toString());
            stmt.setDate(7, issue.getCreationDate() == null ? null : Date.valueOf(issue.getCreationDate()));
            stmt.setObject(8, issue.getExecutor() == null ? null : issue.getExecutor().getId());
            stmt.setObject(9, issue.getReporter() == null ? null : issue.getReporter().getId());
            stmt.setString(10, issue.getWorkFlowCurrentStatus() == null ? null : issue.getWorkFlowCurrentStatus().name());
            stmt.setInt(11, issue.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<User> getMembers(int projectId) {
        List<User> members = new ArrayList<>();

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_MEMBERS_BY_PROJECT_ID, Statement.RETURN_GENERATED_KEYS);
        ) {
            stmt.setInt(1, projectId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next())
                members.add(getUser(rs.getInt(2)));

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return members;
    }

    public ScrumProject getProject(int projectId) {
        ProjectFactoryImpl projectFactory = ProjectFactoryImpl.getInstance();
        ScrumProject project = projectFactory.createProject();

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_PROJECT_BY_ID, Statement.RETURN_GENERATED_KEYS);
        ) {
            stmt.setInt(1, projectId);
            ResultSet rs = stmt.executeQuery();

            rs.next();
            project.setId(rs.getInt(1));
            project.setTitle(rs.getString(2));
            project.setDescription(rs.getString(3));
            project.setDepartmentName(rs.getString(4));
            project.setSupervisor(rs.getObject(5) == null ? null : getUser(rs.getInt(5)));
            project.setAdmin(rs.getObject(6) == null ? null : getUser(rs.getInt(6)));
            project.setSprints(getProjectSprints(rs.getInt(1)));
            project.setProjectBacklog(getBacklog(rs.getInt(1)));
            project.setCurrentSprint(getSprint(rs.getInt(7)));
            project.setMembers(getMembers(rs.getInt(1)));

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return project;
    }

    public List<ProjectSprint> getProjectSprints(int projectId) {
        List<ProjectSprint> projectSprints = new ArrayList<>();

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_SPRINTS_BY_PROJECT_ID, Statement.RETURN_GENERATED_KEYS);
        ) {
            stmt.setInt(1, projectId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                ProjectSprint projectSprint = getSprint(rs.getInt(1));
                projectSprints.add(projectSprint);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return projectSprints;
    }

    public ProjectSprint getSprint(int sprintId) {
        ProjectSprint projectSprint = null;

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_SPRINT_BY_SPRINT_ID, Statement.RETURN_GENERATED_KEYS);
        ) {
            stmt.setInt(1, sprintId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                if (projectSprint == null) projectSprint = SprintFactoryImpl.getInstance().createSprint();
                projectSprint.setId(rs.getInt(1));
                projectSprint.setTitle(rs.getString(3));
                projectSprint.setStartDate(rs.getDate(4) == null ? null : rs.getDate(4).toLocalDate());
                projectSprint.setFinishDate(rs.getDate(5) == null ? null : rs.getDate(5).toLocalDate());
                if (rs.getObject(6) != null) projectSprint.addIssue(getIssue(rs.getInt(6)));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return projectSprint;
    }

    public ProjectBacklog getBacklog(int projectId) {
        ProjectBacklog projectBacklog = BacklogFactoryImpl.getInstance().createBacklog();

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BACKLOG_BY_PROJECT_ID, Statement.RETURN_GENERATED_KEYS);
        ) {
            stmt.setInt(1, projectId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                projectBacklog.setId(rs.getInt(1));
                projectBacklog.setTitle(rs.getString(2));
                if (rs.getObject(4) != null) projectBacklog.addIssue(getIssue(rs.getInt(4)));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return projectBacklog;
    }

    public Issue getIssue(int issueId) {
        IssueFactory issueFactory = IssueFactoryImpl.getInstance();
        Issue issue = null;

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_ISSUE_BY_ID, Statement.RETURN_GENERATED_KEYS);
        ) {
            stmt.setInt(1, issueId);
            ResultSet rs = stmt.executeQuery();

            rs.next();
            issue = issueFactory.createIssue(IssueType.valueOf(rs.getString(2)));
            issue.setId(rs.getInt(1));
            issue.setTitle(rs.getString(3));
            issue.setParentIsue(rs.getObject(4) == null ? null : getIssue(rs.getInt(4)));
            issue.setDescription(rs.getString(7));
            issue.setWorkFlow(rs.getObject(8) == null ? null : getWorkflow(rs.getInt(8)));
            issue.setPriority(rs.getString(9) == null ? null : IssuePriority.valueOf(rs.getString(9)));
            issue.setCreationDate(rs.getDate(10) == null ? null : rs.getDate(10).toLocalDate());
            issue.setExecutor(rs.getObject(11) == null ? null : getUser(rs.getInt(11)));
            issue.setReporter(rs.getObject(12) == null ? null : getUser(rs.getInt(12)));
            issue.setWorkFlowCurrentStatus(rs.getString(13) == null ? null : WorkFlowStatus.valueOf(rs.getString(13)));

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return issue;
    }

    public WorkFlow getWorkflow(int workflowId) {
        WorkFlow workFlow = new WorkFlow();

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_WORKFLOW_BY_ID, Statement.RETURN_GENERATED_KEYS);
        ) {
            stmt.setInt(1, workflowId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                workFlow.setId(rs.getInt(1));
                workFlow.setTitle(rs.getString(2));
                workFlow.add(WorkFlowStatus.valueOf(rs.getString(5)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return workFlow;
    }

    public User getUser(int userId) {
        UserFactory userFactory = UserFactoryImpl.getInstance();
        User user = userFactory.createUser();

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_USER_BY_ID, Statement.RETURN_GENERATED_KEYS);
        ) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            rs.next();
            user.setId(rs.getInt(1));
            user.setFirstName(rs.getString(2));
            user.setLastName(rs.getString(3));
            user.setPatronymic(rs.getString(4));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    public Map<Integer, String> selectProjectList() {
        Map<Integer, String> projects = new HashMap<>();

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_PROJECTS_LIST, Statement.RETURN_GENERATED_KEYS);
        ) {
            ResultSet rs = stmt.executeQuery();

            while (rs.next())
                projects.put(rs.getInt(1), rs.getString(2));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return projects;
    }

    public Map<Integer, String> selectUserList() {
        Map<Integer, String> users = new HashMap<>();

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_USERS_LIST, Statement.RETURN_GENERATED_KEYS);
        ) {
            ResultSet rs = stmt.executeQuery();

            while (rs.next())
                users.put(rs.getInt(1), rs.getString(3) + " " + rs.getString(2) + " " + rs.getString(4));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }

    public Map<Integer, String> selectWorkflowList() {
        Map<Integer, String> workflows = new HashMap<>();

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_WORKFLOWS_LIST, Statement.RETURN_GENERATED_KEYS);
        ) {
            ResultSet rs = stmt.executeQuery();

            while (rs.next())
                workflows.put(rs.getInt(1), rs.getString(2));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return workflows;
    }


    public Integer insertProject(ScrumProject project) {
        Integer generatedId = null;

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_INTO_PROJECTS, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, project.getTitle());
            stmt.setString(2, project.getDescription());
            stmt.setString(3, project.getDepartmentName());
            stmt.setObject(4, project.getSupervisor() == null ? null : project.getSupervisor().getId());
            stmt.setObject(5, project.getAdmin() == null ? null : project.getAdmin().getId());
            stmt.setObject(6, null);

            stmt.executeUpdate();

            ResultSet generatedKeys = stmt.getGeneratedKeys();
            generatedKeys.next();
            generatedId = generatedKeys.getInt(1);

            project.getProjectBacklog().setId(insertBacklog(project.getProjectBacklog(), generatedId));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return generatedId;
    }

    public Integer insertBacklog(ProjectBacklog backlog, int projectId) {
        Integer generatedId = null;

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_INTO_BACKLOGS, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, backlog.getTitle());
            stmt.setInt(2, projectId);

            stmt.executeUpdate();

            ResultSet generatedKeys = stmt.getGeneratedKeys();
            generatedKeys.next();
            generatedId = generatedKeys.getInt(1);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return generatedId;
    }

    public Integer insertUser(User user) {
        Integer generatedId = null;

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_INTO_USERS, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, user.getFirstName());
            stmt.setString(2, user.getLastName());
            stmt.setString(3, user.getPatronymic());

            stmt.executeUpdate();

            ResultSet generatedKeys = stmt.getGeneratedKeys();
            generatedKeys.next();
            generatedId = generatedKeys.getInt(1);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return generatedId;
    }

    public Integer insertProjectMembers(User member, Project project) {
        Integer generatedId = null;

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_INTO_PROJECT_MEMBERS, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, member.getId());
            stmt.setInt(2, project.getId());

            stmt.executeUpdate();

            ResultSet generatedKeys = stmt.getGeneratedKeys();
            generatedKeys.next();
            generatedId = generatedKeys.getInt(1);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return generatedId;
    }

    public Integer insertSprints(Project project, ProjectSprint sprint) {
        Integer generatedId = null;

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_INTO_SPRINTS, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, project.getId());
            stmt.setString(2, sprint.getTitle());
            stmt.setDate(3, sprint.getStartDate() == null ? null : Date.valueOf(sprint.getStartDate()));
            stmt.setDate(4, sprint.getFinishDate() == null ? null : Date.valueOf(sprint.getFinishDate()));

            stmt.executeUpdate();

            ResultSet generatedKeys = stmt.getGeneratedKeys();
            generatedKeys.next();
            generatedId = generatedKeys.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return generatedId;
    }

    public Integer insertWorkflow(WorkFlow workFlow) {
        Integer generatedId = null;

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_INTO_WORKFLOWS, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, workFlow.getTitle());

            stmt.executeUpdate();

            ResultSet generatedKeys = stmt.getGeneratedKeys();
            generatedKeys.next();
            generatedId = generatedKeys.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return generatedId;
    }

    public Integer insertWorkflowStatus(WorkFlow workFlow, String status_name) {
        Integer generatedId = null;

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_INTO_WORKFLOW_STATUSES, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, workFlow.getId());
            stmt.setString(2, status_name);

            stmt.executeUpdate();

            ResultSet generatedKeys = stmt.getGeneratedKeys();
            generatedKeys.next();
            generatedId = generatedKeys.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return generatedId;
    }

    public void deleteFromWorkflowStatus(WorkFlow workFlow, String status_name) {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE_FROM_WORKFLOW_STATUSES)) {
            stmt.setInt(1, workFlow.getId());
            stmt.setString(2, status_name);

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Integer insertIssue(Issue issue, ScrumProject project) {
        Integer generatedId = null;

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_INTO_ISSUES, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, String.valueOf(issue.getIssueType()));
            stmt.setString(2, issue.getTitle());
            stmt.setObject(3, issue.getParentIsue() == null ? null : issue.getParentIsue().getId());

            if (project.getCurrentSprint() != null && project.getCurrentSprint().getIssueList().contains(issue))
                stmt.setObject(4, project.getCurrentSprint().getId());
            else stmt.setObject(4, null);

            if (project.getProjectBacklog().getIssueList().contains(issue))
                stmt.setObject(5, project.getProjectBacklog().getId());
            else stmt.setObject(5, null);

            stmt.setString(6, issue.getDescription());
            stmt.setObject(7, issue.getWorkFlow() == null ? null : issue.getWorkFlow().getId());
            stmt.setString(8, issue.getPriority().toString());
            stmt.setDate(9, issue.getCreationDate() == null ? null : Date.valueOf(issue.getCreationDate()));
            stmt.setObject(10, issue.getExecutor() == null ? null : issue.getExecutor().getId());
            stmt.setObject(11, issue.getReporter() == null ? null : issue.getReporter().getId());
            stmt.setString(12, issue.getWorkFlowCurrentStatus() == null ? null : issue.getWorkFlowCurrentStatus().name());

            stmt.executeUpdate();

            ResultSet generatedKeys = stmt.getGeneratedKeys();
            generatedKeys.next();
            generatedId = generatedKeys.getInt(1);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return generatedId;
    }


    public void removeProjectMembers(User member, Project project) {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE_FROM_PROJECT_MEMBERS)) {
            stmt.setInt(1, member.getId());
            stmt.setInt(2, project.getId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteIssueFromProject(Issue issue) {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE_ISSUE_FROM_PROJECT)) {
            stmt.setInt(1, issue.getId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void moveIssueToSprint(ProjectSprint sprint, Issue issue) {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_ISSUE_MOVE_TO_SPRINT)) {
            stmt.setInt(1, sprint.getId());
            stmt.setInt(2, issue.getId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void moveIssueToBacklog(ProjectBacklog backlog, Issue issue) {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_ISSUE_MOVE_TO_BACKLOG)) {
            stmt.setInt(1, backlog.getId());
            stmt.setInt(2, issue.getId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setCurrentSprint(Project project, ProjectSprint currentSprint) {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_PROJECT_SET_CURRENTSPRINT)) {
            stmt.setInt(1, currentSprint.getId());
            stmt.setInt(2, project.getId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void executeSqlScript(Connection conn, File inputFile) {
        final String splitter = ";";

        Scanner scanner;
        try {
            scanner = new Scanner(inputFile).useDelimiter(splitter);
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
            return;
        }

        Statement currentStatement = null;
        while (scanner.hasNext()) {
            String rawStatement = scanner.next() + splitter;
            try {
                currentStatement = conn.createStatement();
                currentStatement.execute(rawStatement);
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                if (currentStatement != null) {
                    try {
                        currentStatement.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                currentStatement = null;
            }
        }
        scanner.close();
    }
}