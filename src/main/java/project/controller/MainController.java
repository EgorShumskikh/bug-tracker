package project.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import project.model.factory.MainFactory;
import project.model.backlog.ProjectSprint;
import project.filters.ChainOfFilters;
import project.filters.FilterType;
import project.filters.Request;
import project.model.issue.Issue;
import project.model.issue.IssuePriority;
import project.model.issue.IssueType;
import project.model.user.User;
import project.view.backlog.BacklogForm;
import project.view.userview.UserSearchForm;
import project.view.issueview.IssueBox;
import project.view.issueview.IssueState;
import project.view.projectview.ProjectCreateForm;
import project.view.projectview.ProjectEditForm;
import project.view.projectview.ProjectSelectForm;
import project.view.userview.UserCreateForm;
import project.view.userview.UserSelectForm;

import java.net.URL;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class MainController<T> implements Initializable {
    private BacklogForm backlogForm = new BacklogForm(IssueState.BACKLOG);
    public BacklogForm sprintPane = new BacklogForm(IssueState.SPRINT);
    public MainFactory mainFactory = new MainFactory();
    public Request request = new Request();
    private ChainOfFilters chain = new ChainOfFilters();

    @FXML
    private AnchorPane mainPane, firstPane;

    @FXML
    private Label projectId;

    @FXML
    private CheckBox checkPriority, checkBefore, checkAfter, checkTitle, checkReporter, checkExecutor;

    @FXML
    private DatePicker creationBefore, creationAfter, sprintStartDate, sprintFinishDate;

    @FXML
    private Button newIssueInSprint;

    @FXML
    public ListView priority;
    @FXML
    private ListView listTitle;
    @FXML
    private ListView reportersList;
    @FXML
    private ListView executorsList;

    @FXML
    private ScrollPane backlogScrollPane, sprintScrollPane;

    @FXML
    private TextField sprintTitle, textTitle;

    @FXML
    private GridPane sprintGroup;

    @FXML
    private Spinner spinnerSprintId;

    @FXML
    private ChoiceBox typeIssue;

    @FXML
    public MenuItem menuEdit;

    @FXML
    public UserSearchForm myBox;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        firstPane.setVisible(false);

        backlogForm.setStyle("-fx-background-color: ivory");
        sprintPane.setStyle("-fx-background-color: ivory");
        backlogScrollPane.setContent(backlogForm);
        sprintScrollPane.setContent(sprintPane);

        mainPane.widthProperty().addListener((observableValue, number, t1) -> {
            backlogForm.setMinWidth(t1.doubleValue() / 2 - 215);
            sprintPane.setMinWidth(t1.doubleValue() / 2 - 215);
        });

        ObservableList<IssueType> issueTypes = FXCollections.observableArrayList(IssueType.values());
        typeIssue.getItems().setAll(issueTypes);
        typeIssue.getSelectionModel().selectFirst();

        ObservableList<IssuePriority> issuePriorities = FXCollections.observableArrayList(IssuePriority.values());
        priority.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        priority.getItems().setAll(issuePriorities);
        priority.getSelectionModel().selectFirst();

        creationBefore.setValue(LocalDate.now());
        creationAfter.setValue(LocalDate.now());

        myBox.setController(mainFactory);

        spinnerSprintId.getStyleClass().addAll(Spinner.STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL);

    }

    private void changeBacklogPanels(IssueBox issuePanel) {
        switch (IssueState.valueOf(issuePanel.placeProperty.get())) {
            case BACKLOG:
                backlogForm.getChildren().add(issuePanel);
                mainFactory.moveIssueToBacklog(issuePanel.getIssue());
                break;
            case SPRINT:
                sprintPane.getChildren().add(issuePanel);
                mainFactory.moveIssueToSprint(issuePanel.getIssue());
                break;
            case DELETE:
                if (sprintPane.getChildren().contains(issuePanel))
                    sprintPane.getChildren().remove(issuePanel);
                else backlogForm.getChildren().remove(issuePanel);
                mainFactory.deleteIssueFromProject(issuePanel.getIssue());
                break;
        }
    }

    public void newTask(Issue issue, IssueState place) {
        IssueBox issuePane = new IssueBox(issue, place, this);
        changeBacklogPanels(issuePane);
        issuePane.placeProperty.addListener((observableValue, s, t11) -> changeBacklogPanels(issuePane));
    }

    @FXML
    private void changeCurrentSprint() {
        mainFactory.setCurrentSprint(mainFactory.getCurrentProject(), (ProjectSprint) spinnerSprintId.getValue());
        sprintPane.getChildren().clear();
        mainFactory.getCurrentProject().getCurrentSprint().getIssueList().forEach(l -> newTask(l, IssueState.SPRINT));
        sprintTitle.setText(mainFactory.getCurrentProject().getCurrentSprint().getTitle());
        sprintStartDate.setValue(mainFactory.getCurrentProject().getCurrentSprint().getStartDate());
        sprintFinishDate.setValue(mainFactory.getCurrentProject().getCurrentSprint().getFinishDate());
    }

    @FXML
    private void changeStartDate() {
        mainFactory.getCurrentProject().getCurrentSprint().setStartDate(sprintStartDate.getValue());
        mainFactory.updateSprint(mainFactory.getCurrentProject().getCurrentSprint(),
                mainFactory.getCurrentProject().getCurrentSprint().getTitle(),
                sprintStartDate.getValue(),
                mainFactory.getCurrentProject().getCurrentSprint().getFinishDate());
    }

    @FXML
    private void changeFinishDate() {
        mainFactory.getCurrentProject().getCurrentSprint().setFinishDate(sprintFinishDate.getValue());
        mainFactory.updateSprint(mainFactory.getCurrentProject().getCurrentSprint(),
                mainFactory.getCurrentProject().getCurrentSprint().getTitle(),
                mainFactory.getCurrentProject().getCurrentSprint().getStartDate(),
                sprintFinishDate.getValue());
    }

    @FXML
    private void changeTitle() {
        mainFactory.getCurrentProject().getCurrentSprint().setTitle(sprintTitle.getText());
        mainFactory.updateSprint(mainFactory.getCurrentProject().getCurrentSprint(),
                sprintTitle.getText(),
                mainFactory.getCurrentProject().getCurrentSprint().getStartDate(),
                mainFactory.getCurrentProject().getCurrentSprint().getFinishDate());
    }

    @FXML
    public void updateScreenAfterFilter() {
        backlogForm.getChildren().clear();
        List<Issue> pool = chain.startFilter(request, mainFactory.getCurrentProject().getProjectBacklog().getIssueList());
        pool.forEach(l -> newTask(l, IssueState.BACKLOG));

        if (mainFactory.getCurrentProject().getCurrentSprint() != null) {
            sprintPane.getChildren().clear();
            pool = chain.startFilter(request, mainFactory.getCurrentProject().getCurrentSprint().getIssueList());
            pool.forEach(l -> newTask(l, IssueState.SPRINT));
        }
    }

    @FXML
    private void createNewIssueInBacklog() {
        Issue issue = mainFactory.createIssueInBacklog((IssueType) typeIssue.getValue(), null, null, null, null, IssuePriority.HIGH, null, null, null, null);
        newTask(issue, IssueState.BACKLOG);
    }

    @FXML
    private void createNewIssueInSprint() {
        Issue issue = mainFactory.createIssueInBacklog((IssueType) typeIssue.getValue(), null, null, null, null, IssuePriority.HIGH, null, null, null, null);
        newTask(issue, IssueState.SPRINT);
    }

    @FXML
    private void createNewProject() {
        new ProjectCreateForm(this);
    }

    @FXML
    private void editCurrentProject() {
        new ProjectEditForm(this);
    }

    @FXML
    private void selectProject() {
        new ProjectSelectForm("Select project", this);
    }

    @FXML
    private void selectUser() {
        new UserSelectForm("Select user", this);
    }

    @FXML
    public void refresh() {
        firstPane.setVisible(true);
        projectId.setText("Project ID " + mainFactory.getCurrentProject().getId() + " " + mainFactory.getCurrentProject().getTitle());
        updateScreenAfterFilter();

        if (mainFactory.getCurrentProject().getCurrentSprint() == null) {
            sprintPane.getChildren().clear();
            sprintGroup.setDisable(true);
            sprintScrollPane.setDisable(true);
            newIssueInSprint.setDisable(true);
            sprintTitle.clear();
        } else {
            ObservableList<ProjectSprint> spintIds = FXCollections.observableArrayList(mainFactory.getCurrentProject().getSprints());
            SpinnerValueFactory<ProjectSprint> valueFactory = new SpinnerValueFactory.ListSpinnerValueFactory(spintIds);
            valueFactory.setValue(mainFactory.getCurrentProject().getCurrentSprint());
            spinnerSprintId.setValueFactory(valueFactory);

            sprintTitle.setText(mainFactory.getCurrentProject().getCurrentSprint().getTitle());
            sprintStartDate.setValue(mainFactory.getCurrentProject().getCurrentSprint().getStartDate());
            sprintFinishDate.setValue(mainFactory.getCurrentProject().getCurrentSprint().getFinishDate());

            sprintTitle.setText(mainFactory.getCurrentProject().getCurrentSprint().getTitle());
            sprintGroup.setDisable(false);
            sprintScrollPane.setDisable(false);
            newIssueInSprint.setDisable(false);
        }
    }

    @FXML
    private void createNewSprint() {
        mainFactory.createProjectSprint(null, null, null);

        sprintPane.getChildren().clear();

        ObservableList<ProjectSprint> spintIds = FXCollections.observableArrayList(mainFactory.getCurrentProject().getSprints());
        SpinnerValueFactory<ProjectSprint> valueFactory = new SpinnerValueFactory.ListSpinnerValueFactory(spintIds);
        spinnerSprintId.setValueFactory(valueFactory);
        spinnerSprintId.getValueFactory().setValue(mainFactory.getCurrentProject().getCurrentSprint());
        sprintTitle.setText(mainFactory.getCurrentProject().getCurrentSprint().getTitle());
        sprintStartDate.setValue(mainFactory.getCurrentProject().getCurrentSprint().getStartDate());
        sprintFinishDate.setValue(mainFactory.getCurrentProject().getCurrentSprint().getFinishDate());

        sprintGroup.setDisable(false);
        sprintScrollPane.setDisable(false);
        newIssueInSprint.setDisable(false);
    }

    @FXML
    private void createNewUser() {
        new UserCreateForm(mainFactory);
    }

    @FXML
    private void addTitleToList() {
        if (!textTitle.getText().equals("") && !listTitle.getItems().contains(textTitle.getText())) {
            listTitle.getItems().add(textTitle.getText());
            showTitleFilter();
        }
    }

    @FXML
    private void removeTitleFromList() {
        listTitle.getItems().remove(listTitle.getSelectionModel().getSelectedItem());
        showTitleFilter();
    }

    @FXML
    private void addUserToReporters() {
        if (!reportersList.getItems().containsAll(myBox.findListView.getSelectionModel().getSelectedItems()))
            reportersList.getItems().addAll(myBox.findListView.getSelectionModel().getSelectedItems());
        showReporterFilter();
    }

    @FXML
    private void addUserToExecutors() {
        if (!executorsList.getItems().containsAll(myBox.findListView.getSelectionModel().getSelectedItems()))
            executorsList.getItems().addAll(myBox.findListView.getSelectionModel().getSelectedItems());
        showExecutorFilter();
    }

    @FXML
    private void removeUserFromExecutors() {
        executorsList.getItems().removeAll(executorsList.getSelectionModel().getSelectedItems());
        showExecutorFilter();
    }

    @FXML
    private void removeUserFromReporters() {
        reportersList.getItems().removeAll(reportersList.getSelectionModel().getSelectedItems());
        showReporterFilter();
    }

    @FXML
    private void showReporterFilter() {
        if (checkReporter.isSelected() && !reportersList.getItems().isEmpty()) {
            User users[] = new User[reportersList.getItems().size()];
            for (int i = 0; i < reportersList.getItems().size(); i++)
                users[i] = mainFactory.getBdConnect().getUser((int) ((Map.Entry) reportersList.getItems().get(i)).getKey());

            for (int i = 0; i < users.length; i++) {
                System.out.println(users[i]);
            }

            request.add(FilterType.REPORTER, users);
        } else request.remove(FilterType.REPORTER);

        updateScreenAfterFilter();
    }

    @FXML
    private void showExecutorFilter() {
        if (checkExecutor.isSelected() && !executorsList.getItems().isEmpty()) {
            User users[] = new User[executorsList.getItems().size()];
            for (int i = 0; i < executorsList.getItems().size(); i++)
                users[i] = mainFactory.getBdConnect().getUser((int) ((Map.Entry) executorsList.getItems().get(i)).getKey());
            request.add(FilterType.EXECUTOR, users);
        } else request.remove(FilterType.EXECUTOR);

        updateScreenAfterFilter();
    }

    @FXML
    private void showPriorityFilter() {
        if (checkPriority.isSelected()) {
            IssuePriority issuePriorities[] = new IssuePriority[priority.getSelectionModel().getSelectedItems().size()];
            for (int i = 0; i < priority.getSelectionModel().getSelectedItems().size(); i++)
                issuePriorities[i] = (IssuePriority) priority.getSelectionModel().getSelectedItems().get(i);
            request.add(FilterType.PRIORITY, issuePriorities);
        } else
            request.remove(FilterType.PRIORITY);

        updateScreenAfterFilter();
    }

    @FXML
    private void showTitleFilter() {
        if (checkTitle.isSelected() && !listTitle.getItems().isEmpty()) {
            String[] titleMas = new String[listTitle.getItems().size()];
            for (int i = 0; i < listTitle.getItems().size(); i++)
                titleMas[i] = (String) listTitle.getItems().get(i);
            request.add(FilterType.TITLE, titleMas);
        } else
            request.remove(FilterType.TITLE);

        updateScreenAfterFilter();
    }

    @FXML
    private void showCreationBeforeFilter() {
        if (checkBefore.isSelected())
            request.add(FilterType.CREATION_BEFORE, new LocalDate[]{creationBefore.getValue()});
        else request.remove(FilterType.CREATION_BEFORE);

        updateScreenAfterFilter();
    }

    @FXML
    private void showCreationAfterFilter() {
        if (checkAfter.isSelected())
            request.add(FilterType.CREATION_AFTER, new LocalDate[]{creationAfter.getValue()});
        else request.remove(FilterType.CREATION_AFTER);
        updateScreenAfterFilter();
    }
}