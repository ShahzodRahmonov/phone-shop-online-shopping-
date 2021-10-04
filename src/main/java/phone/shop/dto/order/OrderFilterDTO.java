package phone.shop.dto.order;

import lombok.Getter;
import lombok.Setter;
import phone.shop.dto.FilterDTO;

@Setter
@Getter
public class OrderFilterDTO extends FilterDTO {
    private Integer profileId;
    private Integer productId;
    private Integer supplierId;
    private String orderDate;


}
