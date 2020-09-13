package project.model.factory.project;

import project.model.backlog.ProjectBacklog;
import project.model.factory.backlog.BacklogFactoryImpl;
import project.model.project.ScrumProject;

import java.util.ArrayList;

public class ProjectFactoryImpl implements ProjectFactory {

    private static ProjectFactoryImpl factory = new ProjectFactoryImpl();

    public static ProjectFactoryImpl getInstance() {
        return factory;
    }

    @Override
    public ScrumProject createProject() {
        ScrumProject scrumProject = new ScrumProject();

        ProjectBacklog projectBacklog = BacklogFactoryImpl.getInstance().createBacklog();
        projectBacklog.setTitle("Backlog");

        scrumProject.setProjectBacklog(projectBacklog);
        scrumProject.setSprints(new ArrayList<>());
        scrumProject.setMembers(new ArrayList<>());

        return scrumProject;
    }
}
