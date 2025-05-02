package ksh.deliveryhub.common.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.function.Function;

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

    @JsonProperty("hasNext")
    public boolean hasNext() {
        return hasNext;
    }

    public List<T> getContent() {
        return content;
    }
}
