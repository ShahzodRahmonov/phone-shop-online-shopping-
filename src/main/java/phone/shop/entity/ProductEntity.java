package phone.shop.entity;

import lombok.Getter;
import lombok.Setter;
import phone.shop.types.ProductType;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "product")
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "description_uz")
    private String descriptionUz;
    @Column(name = "description_ru")
    private String descriptionRu;
    @Column(name = "description_en")
    private String descriptionEn;

    private String model;
    private ProductType type; // Android / IOS
    private Double price;
    private Double oldPrice;
    private Integer amount;
    private Boolean availability;
    private Double weight;
    private String color;
    private Double screenDiagonal; // Диагональ экрана
    private Boolean isFrontCameraExists; // Фронтальная фотокамера
    private Double frontCameraMp; // Главная фотокамера
    private Integer simCardAmount; // Количество SIM-карт
    private Double batteryCapacity; // Емкость аккумулятора

    @Column(name = "visible", columnDefinition = "boolean default true")
    private Boolean visible;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manufacturer_id")
    private ManufacturerEntity manufacturer; //  ishlab chiqaruvchi
    @Column(name = "created_date")
    private LocalDateTime createdDate;
}
