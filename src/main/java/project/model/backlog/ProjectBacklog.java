package project.model.backlog;

import java.util.Objects;

public class ProjectBacklog extends BaseScrumEntity {

    @Override
    public String toString() {
        return "Backlog{" +
                "title='" + getTitle() + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BaseScrumEntity)) return false;
        BaseScrumEntity baseScrumEntity = (BaseScrumEntity) o;
        return getId() == baseScrumEntity.getId() &&
                Objects.equals(getTitle(), baseScrumEntity.getTitle()) &&
                Objects.equals(getIssueList(), baseScrumEntity.getIssueList());
    }
}
