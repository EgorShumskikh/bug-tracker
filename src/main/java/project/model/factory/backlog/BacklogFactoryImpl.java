package project.model.factory.backlog;

import project.model.backlog.ProjectBacklog;

public class BacklogFactoryImpl implements BacklogFactory {

    private static BacklogFactoryImpl factory = new BacklogFactoryImpl();

    public static BacklogFactoryImpl getInstance() {
        return factory;
    }


    @Override
    public ProjectBacklog createBacklog() {
        ProjectBacklog projectBacklog = new ProjectBacklog();

        return projectBacklog;
    }
}
