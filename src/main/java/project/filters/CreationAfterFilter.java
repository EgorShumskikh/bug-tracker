package project.filters;

import project.model.issue.Issue;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class CreationAfterFilter extends Filter {

    public CreationAfterFilter(Filter filterChain) {
        super(filterChain);
    }

    @Override
    public List<Issue> nextFilter(Request request, List<Issue> issueList) {
        pool = issueList;

        if (request.get(FilterType.CREATION_AFTER) != null) {
            pool = new ArrayList<>();

            LocalDate finalDate = Arrays.stream((LocalDate[]) request.get(FilterType.CREATION_AFTER))
                    .max(Comparator.naturalOrder())
                    .get();

            pool.addAll(issueList
                    .stream()
                    .filter(l -> Optional.ofNullable(l.getCreationDate()).orElse(LocalDate.of(1970, 1, 1)).compareTo((finalDate)) >= 0)
                    .collect(Collectors.toList()));
        }

        super.nextFilter(request, pool);

        return pool;
    }
}