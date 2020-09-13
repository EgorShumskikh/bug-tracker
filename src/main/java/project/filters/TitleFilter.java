package project.filters;
import project.model.issue.Issue;

import java.util.*;
import java.util.stream.Collectors;

public class TitleFilter extends Filter {

    public TitleFilter(Filter filterChain) {
        super(filterChain);
    }

    @Override
    public List<Issue> nextFilter(Request request, List<Issue> issueList) {
        pool = issueList;

       if (request.get(FilterType.TITLE)!=null) {

           Set<String> set = new HashSet<>(Arrays.asList((String[]) request.get(FilterType.TITLE)));
           String[] result = set.toArray(new String[set.size()]);
           request.add(FilterType.TITLE,result);

            pool = new ArrayList<>();

            Arrays.asList(request.get(FilterType.TITLE)).forEach(m ->
                    pool.addAll(issueList
                            .stream()
                            .filter(l -> Optional.ofNullable(l.getTitle()).orElse("").equals(m))
                            .collect(Collectors.toList())));
        }

        super.nextFilter(request, pool);

        return pool;
    }
}