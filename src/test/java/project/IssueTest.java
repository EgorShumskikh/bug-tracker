package project;

import org.junit.Test;
import project.model.issue.Issue;
import project.model.issue.projectissue.StoryIssue;
import project.model.workflow.WorkFlow;
import project.model.workflow.WorkFlowStatus;

import static junit.framework.TestCase.assertTrue;

public class IssueTest {

    @Test
    public void tes_setNextStatus_set_one() {
        Issue issue = new StoryIssue();
        WorkFlow newWorkFlow = new WorkFlow();
        newWorkFlow.add(WorkFlowStatus.OPEN_ISSUE);
        newWorkFlow.add(WorkFlowStatus.REVIEW_ISSUE);
        newWorkFlow.add(WorkFlowStatus.CLOSE_ISSUE);
        issue.setWorkFlow(newWorkFlow);
        issue.setWorkFlowCurrentStatus(WorkFlowStatus.OPEN_ISSUE);
        issue.setNextStatus();

        assertTrue(issue.getWorkFlowCurrentStatus().equals(WorkFlowStatus.REVIEW_ISSUE));
    }

    @Test
    public void tes_setNextStatus_set_any() {
        Issue issue = new StoryIssue();
        WorkFlow newWorkFlow = new WorkFlow();
        newWorkFlow.add(WorkFlowStatus.OPEN_ISSUE);
        newWorkFlow.add(WorkFlowStatus.REVIEW_ISSUE);
        newWorkFlow.add(WorkFlowStatus.CLOSE_ISSUE);
        issue.setWorkFlow(newWorkFlow);
        issue.setWorkFlowCurrentStatus(WorkFlowStatus.OPEN_ISSUE);
        for (int i = 0; i < 10; i++)
            issue.setNextStatus();

        assertTrue(issue.getWorkFlowCurrentStatus().equals(WorkFlowStatus.CLOSE_ISSUE));
    }

    @Test
    public void tes_setPrevious_set_next() {
        Issue issue = new StoryIssue();
        WorkFlow newWorkFlow = new WorkFlow();
        newWorkFlow.add(WorkFlowStatus.OPEN_ISSUE);
        newWorkFlow.add(WorkFlowStatus.REVIEW_ISSUE);
        newWorkFlow.add(WorkFlowStatus.CLOSE_ISSUE);
        issue.setWorkFlow(newWorkFlow);
        issue.setWorkFlowCurrentStatus(WorkFlowStatus.CLOSE_ISSUE);
        issue.setPreviousStatus();

        assertTrue(issue.getWorkFlowCurrentStatus().equals(WorkFlowStatus.REVIEW_ISSUE));
    }

    @Test
    public void tes_setPreviousStatus_set_any() {
        Issue issue = new StoryIssue();
        WorkFlow newWorkFlow = new WorkFlow();
        newWorkFlow.add(WorkFlowStatus.OPEN_ISSUE);
        newWorkFlow.add(WorkFlowStatus.REVIEW_ISSUE);
        newWorkFlow.add(WorkFlowStatus.CLOSE_ISSUE);
        issue.setWorkFlow(newWorkFlow);
        issue.setWorkFlowCurrentStatus(WorkFlowStatus.CLOSE_ISSUE);
        for (int i = 0; i < 10; i++)
            issue.setPreviousStatus();

        assertTrue(issue.getWorkFlowCurrentStatus().equals(WorkFlowStatus.OPEN_ISSUE));
    }
}
