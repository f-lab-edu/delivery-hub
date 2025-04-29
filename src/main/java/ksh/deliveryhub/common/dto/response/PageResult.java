package ksh.deliveryhub.common.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class PageResult<T> {

    private boolean hasNext;
    private List<T> content;

    public static <T> PageResult<T> of(boolean hasNext, List<T> content) {
        return new PageResult<>(hasNext, content);
    }
}
