package ru.practicum.ewm.main.common;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;

public class MyPageRequest extends PageRequest {
    private final int from;

    protected MyPageRequest(int from, int size, @NonNull Sort sort) {
        super(from / size, size, sort);
        Assert.notNull(sort, "Sort must not be null");
        this.from = from;
    }

    public static MyPageRequest of(int from, int size, @NonNull Sort sort) {
        if (sort.isEmpty()) {
            sort = Sort.unsorted();
        }
        return new MyPageRequest(from, size, sort);
    }

    public static MyPageRequest of(int from, int size) {
        return of(from, size, Sort.unsorted());
    }

    @Override
    public long getOffset() {
        return from;
    }
}