package project;

import project.model.project.Project;
import project.model.project.ScrumProject;
import project.model.user.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class ProjectTest {

    private Project project;
    private User user1, user2;

    @Before
    public void setUp() {
        project = new ScrumProject();
        project.setMembers(new ArrayList<>());
        user1 = new User();
        user1.setId(1);
        user2 = new User();
        user2.setId(2);
    }

    @After
    public void tearDown() {
        project.getMembers().clear();
    }

    @Test
    public void tes_project_addMember() {
        project.addMember(user1);
        project.addMember(user2);
        assertThat(project.getMembers(), contains(user1, user2));
    }

    @Test
    public void tes_project_addMember_not_have_dublicates() {
        project.addMember(user1);
        project.addMember(user2);
        project.addMember(user2);
        project.addMember(user2);
        project.addMember(user1);

        assertThat(project.getMembers(), hasSize(2));
    }

    @Test
    public void tes_project_removeMember() {
        project.addMember(user1);
        project.addMember(user2);
        project.removeMember(user1);
        project.removeMember(user2);
        project.removeMember(user2);

        assertThat(project.getMembers(), not(contains(user1, user2)));
    }
}
