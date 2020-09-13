package project.model.factory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import project.jdbс.DBConnect;
import project.jdbс.DBConnectToMemory;
import project.model.backlog.ProjectSprint;
import project.model.factory.issue.IssueFactory;
import project.model.factory.issue.IssueFactoryImpl;
import project.model.factory.project.ProjectFactory;
import project.model.factory.project.ProjectFactoryImpl;
import project.model.factory.sprint.SprintFactoryImpl;
import project.model.factory.user.UserFactoryImpl;
import project.model.factory.user.UserFactory;
import project.model.issue.Issue;
import project.model.issue.IssuePriority;
import project.model.issue.IssueType;
import project.model.project.ScrumProject;
import project.model.user.User;
import project.model.workflow.WorkFlow;
import project.model.workflow.WorkFlowStatus;

import java.time.LocalDate;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;

public class MainFactory {
    private DBConnect bdConnect = new DBConnectToMemory();
    private static final Logger LOGGER = LoggerFactory.getLogger(MainFactory.class);
    private ScrumProject currentProject;
    private UserFactory userFactory = UserFactoryImpl.getInstance();
    private ProjectFactory projectFactory = ProjectFactoryImpl.getInstance();
    private SprintFactoryImpl sprintFactory = SprintFactoryImpl.getInstance();
    private IssueFactory issueFactory = IssueFactoryImpl.getInstance();

    public ScrumProject loadProject(int projectId) {
        return bdConnect.getProject(projectId);
    }

    public User createUser(String firstname, String lastname, String patronymic) {
        User user = userFactory.createUser();
        user.setAllUserAttributes(firstname, lastname, patronymic);
        Integer generatedId = bdConnect.insertUser(user);
        user.setId(generatedId);
        LOGGER.info("Create " + user);
        return user;
    }

    public void updateUser(User user, String firstname, String lastname, String patronymic) {
        user.setAllUserAttributes(firstname, lastname, patronymic);
        bdConnect.updateUser(user);
        LOGGER.info("New values are set for " + user);
    }

    public boolean addMemberToProject(User member) {
        if (currentProject.getMembers().contains(member)) return false;
        bdConnect.insertProjectMembers(member, currentProject);
        LOGGER.info(member + " added to " + currentProject);
        return currentProject.addMember(member);
    }

    public boolean removeMemberFromProject(User member) {
        if (currentProject.getMembers().contains(member)) {
            bdConnect.removeProjectMembers(member, currentProject);
            LOGGER.info(member + " removed from " + currentProject);
        } else
            LOGGER.info("Could not remove " + member + " from " + currentProject);
        return currentProject.removeMember(member);
    }

    public ScrumProject createProject(String title, String description, String departmentName, User supervisor, User admin) {
        ScrumProject project = projectFactory.createProject();
        project.setAllProjectAttributes(title, description, departmentName, supervisor, admin);
        Integer generatedProjectId = bdConnect.insertProject(project);
        project.setId(generatedProjectId);
        currentProject = project;
        LOGGER.info("Create " + project);
        return project;
    }

    public void updateProgect(ScrumProject project, String title, String description, String departmentName, User supervisor, User admin) {
        project.setAllProjectAttributes(title, description, departmentName, supervisor, admin);
        bdConnect.updateProject(project);
        LOGGER.info("New values are set in " + project);
    }

    public void updateSprint(ProjectSprint projectSprint, String title, LocalDate startDate, LocalDate finishDate) {
        projectSprint.setTitle(title);
        projectSprint.setStartDate(startDate);
        projectSprint.setFinishDate(finishDate);
        bdConnect.updateSprint(projectSprint);
        LOGGER.info("New values are set in " + projectSprint);
    }

    public ProjectSprint createProjectSprint(String title, LocalDate startDate, LocalDate finishDate) {
        ProjectSprint projectSprint = sprintFactory.createSprint();
        projectSprint.setTitle(title);
        projectSprint.setStartDate(startDate);
        projectSprint.setFinishDate(finishDate);
        Integer generatedId = bdConnect.insertSprints(currentProject, projectSprint);
        projectSprint.setId(generatedId);
        currentProject.addSprint(projectSprint);
        currentProject.setCurrentSprint(projectSprint);
        bdConnect.setCurrentSprint(currentProject, projectSprint);
        LOGGER.info("Create " + projectSprint);
        return projectSprint;
    }

    public Issue createIssueInBacklog(IssueType issueType, String title, Issue parentIssue, String description, WorkFlow workflow,
                                      IssuePriority issuePriority, LocalDate creationDate, User executor, User reporter, WorkFlowStatus currentWorkflowStatus) {
        Issue issue = issueFactory.createIssue(issueType);
        WorkFlow standartWorkFlow = createWorkflow("Standart Workflow");
        addStatusToWorkFlow(standartWorkFlow, WorkFlowStatus.OPEN_ISSUE);
        addStatusToWorkFlow(standartWorkFlow, WorkFlowStatus.CLOSE_ISSUE);
        if (workflow==null) workflow=standartWorkFlow;
        if (currentWorkflowStatus==null) currentWorkflowStatus=WorkFlowStatus.OPEN_ISSUE;
        issue.setAllIssueAttributes(issueType, title, parentIssue, description, workflow, issuePriority, creationDate, executor, reporter, currentWorkflowStatus);
        currentProject.getProjectBacklog().addIssue(issue);
        Integer generatedId = bdConnect.insertIssue(issue, getCurrentProject());
        issue.setId(generatedId);
        LOGGER.info("Create " + issue);
        return issue;
    }

    public void updateIssue(Issue issue, IssueType issueType, String title, Issue parentIssue, String description, WorkFlow workflow,
                            IssuePriority issuePriority, LocalDate creationDate, User executor, User reporter, WorkFlowStatus currentWorkflowStatus) {
        issue.setAllIssueAttributes(issueType, title, parentIssue, description, workflow, issuePriority, creationDate, executor, reporter, currentWorkflowStatus);
        bdConnect.updateIssue(issue);
        LOGGER.info("New values are set in " + issue);
    }

    public Issue createChildIssue(Issue issue, String title) {
        Issue childIssue = createIssueInBacklog(issue.getChildType(), title, issue, issue.getDescription(), issue.getWorkFlow(),
                issue.getPriority(), issue.getCreationDate(), issue.getExecutor(), issue.getReporter(), issue.getWorkFlowCurrentStatus());

        return childIssue;
    }

    public Map.Entry<Integer, String> getMapEntryFromUser(User user) {
        if (user==null) return null;
        Integer userId = user.getId();
        String userName =  user.getLastName() + " " + user.getFirstName() + " " + user.getPatronymic();
        return new AbstractMap.SimpleEntry(userId, userName);
    }

    public User getUserFromMapEntry(Map.Entry<Integer, String> userMapEntry) {
        if (userMapEntry == null) return null;
        return getBdConnect().getUser(userMapEntry.getKey());
    }

    public boolean deleteIssueFromProject(Issue issue) {
        bdConnect.deleteIssueFromProject(issue);
        if (currentProject.getProjectBacklog().getIssueList().contains(issue)) {
            LOGGER.info("Removed " + issue + " from " + currentProject.getProjectBacklog());
            return currentProject.getProjectBacklog().removeIssue(issue);
        } else
            for (ProjectSprint e : currentProject.getSprints())
                if (e.getIssueList().contains(issue)) {
                    LOGGER.info("Removed " + issue + " from " + e);
                    return e.removeIssue(issue);
                }
        LOGGER.info("Could not remove issue" + issue);
        return false;
    }

    public void showFilter(List<?> issueList, String text) {
        LOGGER.info(text);
        if (issueList.stream().count() == 0) LOGGER.debug("List is empty");
        else
            issueList.forEach(l -> LOGGER.info("Issue" + l));
        LOGGER.info("List is formed");
    }

    public boolean moveIssueToSprint(Issue issue) {
        bdConnect.moveIssueToSprint(currentProject.getCurrentSprint(), issue);
        if (currentProject.getProjectBacklog().getIssueList().contains(issue)) {
            LOGGER.info("Moved to Sprint" + issue);
            return currentProject.getProjectBacklog().removeIssue(issue) & currentProject.getCurrentSprint().addIssue(issue);
        } else
            return false;
    }

    public boolean moveIssueToBacklog(Issue issue) {
        bdConnect.moveIssueToBacklog(currentProject.getProjectBacklog(), issue);
        if (currentProject.getCurrentSprint() == null) return false;
        if (currentProject.getCurrentSprint().getIssueList().contains(issue)) {
            LOGGER.info("Moved to Backlog " + issue);
            return currentProject.getCurrentSprint().removeIssue(issue) & currentProject.getProjectBacklog().addIssue(issue);
        }
        return false;
    }

    public WorkFlow createWorkflow(String title) {
        WorkFlow workFlow = new WorkFlow(title);
        Integer generatedId = bdConnect.insertWorkflow(workFlow);
        workFlow.setId(generatedId);
        LOGGER.info("Create " + workFlow);
        return workFlow;
    }

    public boolean addStatusToWorkFlow(WorkFlow workFlow, WorkFlowStatus status) {
        if (workFlow.getWorkFlowList().contains(status)) return false;
        bdConnect.insertWorkflowStatus(workFlow, status.name());
        LOGGER.info("Status  " + status + " add to " + workFlow);
        return workFlow.add(status);
    }

    public boolean removeStatusFromWorkFlow(WorkFlow workFlow, WorkFlowStatus status) {
        bdConnect.deleteFromWorkflowStatus(workFlow, status.name());
        LOGGER.info("Status " + status + " removed from " + workFlow);
        return workFlow.remove(status);
    }

    public WorkFlowStatus setNextIssueStatus(Issue issue) {
        WorkFlowStatus newStatus = issue.setNextStatus();
        bdConnect.updateIssue(issue);
        return newStatus;
    }

    public WorkFlowStatus setPreviousIssueStatus(Issue issue) {
        WorkFlowStatus newStatus = issue.setPreviousStatus();
        bdConnect.updateIssue(issue);
        return newStatus;
    }

    public ScrumProject getCurrentProject() {
        return currentProject;
    }

    public void setCurrentSprint(ScrumProject project, ProjectSprint sprint) {
        project.setCurrentSprint(sprint);
        bdConnect.updateCurrentSprint(project, sprint);
    }

    public void setCurrentProject(ScrumProject currentProject) {
        this.currentProject = currentProject;
    }

    public Map<Integer, String> getUsers() {
        return bdConnect.selectUserList();
    }

    public Map<Integer, String> getWorkflows() {
        return bdConnect.selectWorkflowList();
    }

    public Map<Integer, String> getProjects() {
        return bdConnect.selectProjectList();
    }

    public DBConnect getBdConnect() {
        return bdConnect;
    }
}