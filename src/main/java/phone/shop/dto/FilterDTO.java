package phone.shop.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Sort;

import javax.persistence.criteria.CriteriaBuilder;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
public class FilterDTO {
    private Integer page = 0;
    private Integer size = 10;

    private String sortBy = "createdDate";
    private Sort.Direction direction = Sort.Direction.DESC;
}
