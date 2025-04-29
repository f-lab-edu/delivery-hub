package ksh.deliveryhub.common.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.function.Function;

@Getter
@AllArgsConstructor
public class PageResult<T> {

    private boolean hasNext;
    private List<T> content;

    public static <T> PageResult<T> of(boolean hasNext, List<T> content) {
        return new PageResult<>(hasNext, content);
    }

    public <U> PageResult<U> map(Function<T, U> mapper) {
        List<U> mappedContent = content.stream()
            .map(mapper)
            .toList();

        return new PageResult<>(hasNext, mappedContent);
    }
}
