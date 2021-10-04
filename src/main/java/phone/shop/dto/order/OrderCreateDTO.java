package phone.shop.dto.order;

import lombok.Getter;
import lombok.Setter;
import phone.shop.types.PaymentType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
public class OrderCreateDTO {
    private Integer id;
    private String requirement;
    private String address;
    @NotBlank
    private String contact;
    @NotNull
    private PaymentType paymentType;
    @Size(min = 1)
    private List<OrderItemCreateDTO> itemList;


}
