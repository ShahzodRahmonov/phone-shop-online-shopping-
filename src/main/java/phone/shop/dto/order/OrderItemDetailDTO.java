package phone.shop.dto.order;

import lombok.Getter;
import lombok.Setter;
import phone.shop.dto.product.ProductDetailDTO;

import java.time.LocalDateTime;

@Getter
@Setter
public class OrderItemDetailDTO {
    private Integer id;
    private Integer orderId;
    private ProductDetailDTO product;
    private Integer amount;
    private LocalDateTime createdDate;
    private double price;
}
