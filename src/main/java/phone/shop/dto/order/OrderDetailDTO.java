package phone.shop.dto.order;

import lombok.Getter;
import lombok.Setter;
import phone.shop.dto.profile.ProfileDetailDTO;
import phone.shop.dto.subpplier.SupplierDetailDTO;
import phone.shop.entity.SupplierEntity;
import phone.shop.types.OrderStatus;
import phone.shop.types.PaymentType;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
public class OrderDetailDTO {
    private Integer id;
    private ProfileDetailDTO profile;

    private String requirement;
    private String address;
    private String contact;

    private Double deliveryCost;
    private PaymentType paymentType;
    private OrderStatus status;
    private SupplierDetailDTO supplier;

    private LocalDateTime deliveryDate;
    private LocalDateTime createdDate;

    private List<OrderItemDetailDTO> orderItemList;

}
