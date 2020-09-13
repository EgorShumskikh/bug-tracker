package project.filters;

import project.model.issue.Issue;
import project.model.user.User;

import java.util.*;
import java.util.stream.Collectors;

public class ReporterFilter extends Filter {

    public ReporterFilter(Filter filterChain) {
        super(filterChain);
    }

    @Override
    public List<Issue> nextFilter(Request request, List<Issue> issueList) {
        pool = issueList;

        if (request.get(FilterType.REPORTER)!=null) {

            Set<User> set = new HashSet<>(Arrays.asList((User[]) request.get(FilterType.REPORTER)));
            User[] result = set.toArray(new User[set.size()]);
            request.add(FilterType.REPORTER,result);

            pool = new ArrayList<>();

            Arrays.asList(request.get(FilterType.REPORTER)).forEach(m ->
                    pool.addAll(issueList
                            .stream()
                            .filter(l ->m.equals(l.getReporter()))
                            .collect(Collectors.toList())));
        }

        super.nextFilter(request, pool);

        return pool;
    }
}