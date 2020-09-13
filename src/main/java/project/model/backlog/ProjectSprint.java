package project.model.backlog;

import java.time.LocalDate;
import java.util.Objects;

public class ProjectSprint extends BaseScrumEntity {

    private LocalDate startDate;
    private LocalDate finishDate;

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(LocalDate finishDate) {
        this.finishDate = finishDate;
    }

    @Override
    public String toString() {
        return "id="+getId();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BaseScrumEntity)) return false;
        ProjectSprint sprint = (ProjectSprint) o;
        return getId() == sprint.getId() &&
                Objects.equals(getTitle(), sprint.getTitle()) &&
                Objects.equals(startDate, sprint.startDate) &&
                Objects.equals(finishDate, sprint.finishDate) &&
                Objects.equals(getIssueList(), sprint.getIssueList());
    }
}