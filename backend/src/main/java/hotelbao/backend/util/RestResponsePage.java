package hotelbao.backend.util;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RestResponsePage<T> extends PageImpl<T> {
    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public RestResponsePage(
        @JsonProperty("content") List<T> content,
        @JsonProperty("number") int page,
        @JsonProperty("size") int size,
        @JsonProperty("totalElements") long totalElements
    ) {
        super(content, size > 0 ? PageRequest.of(page, size) : Pageable.unpaged(), totalElements);
    }
}
