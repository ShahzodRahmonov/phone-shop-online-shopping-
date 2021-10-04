package phone.shop.dto.order;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Setter
@Getter
public class OrderItemCreateDTO {
    private Integer id;
    @NotNull
    private Integer productId;
    @NotNull
    private Integer amount;
}
