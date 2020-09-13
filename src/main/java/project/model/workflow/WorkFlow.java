package project.model.workflow;

import project.model.BaseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class WorkFlow extends BaseEntity {
    protected String title;
    protected List<WorkFlowStatus> workFlowList;

    public WorkFlow() {
        workFlowList = new ArrayList<>();
    }

    public WorkFlow(String title) {
        workFlowList = new ArrayList<>();
        this.title = title;
    }

    public boolean add(WorkFlowStatus workFlowStatus) {
        return workFlowList.add(workFlowStatus);
    }

    public boolean remove(WorkFlowStatus workFlowStatus) {
        return workFlowList.remove(workFlowStatus);
    }

    public List<WorkFlowStatus> getWorkFlowList() {
        return workFlowList.stream().collect(Collectors.toList());
    }

    public void setWorkFlowList(List<WorkFlowStatus> workFlowList) {
        this.workFlowList = workFlowList;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "WorkFlow{" +
                "title='" + title + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WorkFlow)) return false;
        WorkFlow workFlow = (WorkFlow) o;

        return id == workFlow.id &&
                Objects.equals(title, workFlow.title) &&
                Objects.equals(workFlowList, workFlow.workFlowList);
    }
}