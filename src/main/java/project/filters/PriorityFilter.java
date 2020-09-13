package project.filters;

import project.model.issue.Issue;
import project.model.issue.IssuePriority;

import java.util.*;
import java.util.stream.Collectors;

public class PriorityFilter extends Filter {

    public PriorityFilter(Filter filterChain) {
        super(filterChain);
    }

    @Override
    public List<Issue> nextFilter(Request request, List<Issue> issueList) {
        pool = issueList;

        if (request.get(FilterType.PRIORITY)!=null) {
            pool = new ArrayList<>();

            Arrays.asList((IssuePriority[]) request.get(FilterType.PRIORITY)).forEach(m ->
                    pool.addAll(issueList
                            .stream()
                            .filter(l -> l.getPriority() ==  m)
                            .collect(Collectors.toList())));
        }

        super.nextFilter(request, pool);

        return pool;
    }
}