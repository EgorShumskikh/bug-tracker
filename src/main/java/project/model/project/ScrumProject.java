package project.model.project;

import project.model.backlog.ProjectBacklog;
import project.model.backlog.ProjectSprint;
import project.model.issue.Issue;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ScrumProject extends Project {
    private ProjectBacklog projectBacklog;
    private List<ProjectSprint> sprints;
    private ProjectSprint currentSprint;

    public ProjectBacklog getProjectBacklog() {
        return projectBacklog;
    }

    public void setProjectBacklog(ProjectBacklog projectBacklog) {
        this.projectBacklog = projectBacklog;
    }

    public List<ProjectSprint> getSprints() {
        return sprints.stream().collect(Collectors.toList());
    }

    public void setSprints(List<ProjectSprint> sprints) {
        this.sprints = sprints;
    }

    public boolean addSprint(ProjectSprint projectSprint) {
        return sprints.add(projectSprint);
    }

    public boolean removeSprint(ProjectSprint projectSprint) {
        return sprints.remove(projectSprint);
    }

    public boolean moveIssueToSprint(Issue issue) {
        return projectBacklog.removeIssue(issue) & currentSprint.addIssue(issue);
    }

    public boolean moveIssueToBacklog(Issue projectIssue) {
        return currentSprint.removeIssue(projectIssue) & projectBacklog.addIssue(projectIssue);
    }

    public ProjectSprint getCurrentSprint() {
        return currentSprint;
    }

    public void setCurrentSprint(ProjectSprint currentSprint) {
        this.currentSprint = currentSprint;
    }

    @Override
    public String toString() {
        return "Project{" +
                "id=" + getId() +
                ", title='" + getTitle() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", departmentName='" + getDepartmentName() + '\'' +
                ", supervisor='" + getSupervisor() + '\'' +
                ", admin='" + getAdmin() + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ScrumProject)) return false;
        ScrumProject project = (ScrumProject) o;
        return id == project.id &&
                Objects.equals(getTitle(), project.getTitle()) &&
                Objects.equals(getDescription(), project.getDescription()) &&
                Objects.equals(getDepartmentName(), project.getDepartmentName()) &&
                Objects.equals(getSupervisor(), project.getSupervisor()) &&
                Objects.equals(getAdmin(), project.getAdmin()) &&
                Objects.equals(getSprints(), project.getSprints()) &&
                Objects.equals(getProjectBacklog(), project.getProjectBacklog()) &&
                Objects.equals(getCurrentSprint(), project.getCurrentSprint()) &&
                Objects.equals(getMembers(), project.getMembers());
    }
}