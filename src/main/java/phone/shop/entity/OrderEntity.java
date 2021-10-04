package phone.shop.entity;

import lombok.Getter;
import lombok.Setter;
import phone.shop.types.OrderStatus;
import phone.shop.types.PaymentType;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "order_table")
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "profile_id")
    private Integer profileId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", insertable = false, updatable = false)
    private ProfileEntity profile;

    @Column(name = "delivery_date")
    private LocalDateTime deliveryDate;

    @Column(name = "requirement")
    private String requirement;
    @Column
    private String address;
    @Column
    private String contact;
    @Column
    private Double deliveryCost;
    @Column(name = "payment_type")
    private PaymentType paymentType;
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private OrderStatus status;

    @Column(name = "supplier_id")
    private Integer supplierId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id", insertable = false, updatable = false)
    private SupplierEntity supplier;

    @Column(name = "created_date")
    private LocalDateTime createdDate;
    @Column(name = "delivered_date")
    private LocalDateTime deliveredDate; // yetkazib berilgan sana
}
