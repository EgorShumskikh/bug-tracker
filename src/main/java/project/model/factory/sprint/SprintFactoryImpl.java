package project.model.factory.sprint;

import project.model.backlog.ProjectSprint;

public class SprintFactoryImpl implements SprintFactory {

    private static SprintFactoryImpl factory = new SprintFactoryImpl();

    public static SprintFactoryImpl getInstance() {
        return factory;
    }

    @Override
    public ProjectSprint createSprint() {
        ProjectSprint projectSprint = new ProjectSprint();

        return projectSprint;
    }
}

