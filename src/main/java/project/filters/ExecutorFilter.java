package project.filters;

import project.model.issue.Issue;
import project.model.user.User;

import java.util.*;
import java.util.stream.Collectors;

public class ExecutorFilter extends Filter {

    public ExecutorFilter(Filter filterChain) {
        super(filterChain);
    }

    @Override
    public List<Issue> nextFilter(Request request, List<Issue> issueList) {
        pool = issueList;

        if (request.get(FilterType.EXECUTOR)!=null) {

            Set<User> set = new HashSet<>(Arrays.asList((User[]) request.get(FilterType.EXECUTOR)));
            User[] result = set.toArray(new User[set.size()]);
            request.add(FilterType.EXECUTOR,result);

            pool = new ArrayList<>();

            Arrays.asList((User[]) request.get(FilterType.EXECUTOR)).forEach(m ->
                    pool.addAll(issueList
                            .stream()
                            .filter(l -> m.equals(l.getExecutor()))
                            .collect(Collectors.toList())));
        }

        super.nextFilter(request, pool);

        return pool;
    }
}