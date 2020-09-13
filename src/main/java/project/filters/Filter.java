package project.filters;


import project.model.issue.Issue;

import java.util.List;

public abstract class Filter {

    protected List<Issue> pool;

    private Filter filterChain;

    public Filter(Filter filterChain) {
        this.filterChain = filterChain;
    }

    public List<Issue> nextFilter(Request request, List<Issue> issueList) {
        pool = issueList;

        if (filterChain != null)
            pool = filterChain.nextFilter(request, issueList);

        return pool;
    }
}