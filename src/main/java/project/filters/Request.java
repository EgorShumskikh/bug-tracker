package project.filters;

import java.util.HashMap;
import java.util.Map;

public class  Request<T> {

    private Map<FilterType, T[] > filters = new HashMap<>();

    public Request() {
    }

    public void add(FilterType filterType, T[] list){
        filters.put(filterType, list);
    }

    public void remove(FilterType filterType){
        filters.remove(filterType);
    }

    public T[] get(FilterType filterType){
        return filters.get(filterType);
    }
}