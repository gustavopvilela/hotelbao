package hotelbao.backend.util;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RestResponsePage<T> extends PageImpl<T> {

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public RestResponsePage(
            @JsonProperty("content") List<T> content,
            @JsonProperty("page") Map<String, Object> pageInfo
    ) {
        super(content, createPageable(pageInfo), getTotalElements(pageInfo));
    }

    private static Pageable createPageable(Map<String, Object> pageInfo) {
        if (pageInfo == null) {
            return Pageable.unpaged();
        }

        int number = (Integer) pageInfo.getOrDefault("number", 0);
        int size = (Integer) pageInfo.getOrDefault("size", 20);

        return PageRequest.of(number, size);
    }

    private static long getTotalElements(Map<String, Object> pageInfo) {
        if (pageInfo == null) {
            return 0;
        }

        Object totalElements = pageInfo.get("totalElements");
        if (totalElements instanceof Integer) {
            return ((Integer) totalElements).longValue();
        } else if (totalElements instanceof Long) {
            return (Long) totalElements;
        }
        return 0;
    }
}