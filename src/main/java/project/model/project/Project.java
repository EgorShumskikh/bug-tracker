package project.model.project;

import project.model.BaseEntity;
import project.model.user.User;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public abstract class Project extends BaseEntity {
    private String title;
    private String description;
    private String departmentName;
    private User supervisor;
    private User admin;
    private List<User> members;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public User getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(User supervisor) {
        this.supervisor = supervisor;
    }

    public User getAdmin() {
        return admin;
    }

    public void setAdmin(User admin) {
        this.admin = admin;
    }

    public List<User> getMembers() {
        return members.stream().collect(Collectors.toList());
    }

    public boolean addMember(User user) {
        if (!members.contains(user))
            return members.add(user);
        else
            return false;
    }

    public boolean removeMember(User user) {
        return members.remove(user);
    }

    public void setMembers(List<User> members) {
        this.members = members;
    }

    public void setAllProjectAttributes(String title, String description,String departmentName,User supervisor, User admin) {
        this.title = title;
        this.description=description;
        this.departmentName=departmentName;
        this.supervisor=supervisor;
        this.admin=admin;
    }
}