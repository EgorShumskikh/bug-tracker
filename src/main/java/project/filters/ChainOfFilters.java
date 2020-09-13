package project.filters;

import project.model.issue.Issue;

import java.util.List;

public class ChainOfFilters {

    private Filter filterChain;


    public ChainOfFilters() {
        filterChain = new PriorityFilter(new ExecutorFilter(new TitleFilter(new ReporterFilter(new CreationAfterFilter(new CreationBeforeFilter(null))))));
    }

    public List<Issue> startFilter(Request request, List<Issue> issueList) {
        return filterChain.nextFilter(request, issueList);
    }
}