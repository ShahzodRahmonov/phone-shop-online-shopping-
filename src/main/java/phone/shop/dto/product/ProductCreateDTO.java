package phone.shop.dto.product;

import lombok.Getter;
import lombok.Setter;
import phone.shop.dto.ImageDTO;
import phone.shop.types.ProductType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
public class ProductCreateDTO {
    @NotBlank
    private String name;
    @NotBlank
    private String descriptionUz;
    @NotBlank
    private String descriptionRu;
    @NotBlank
    private String descriptionEn;
    @NotBlank
    private String model;
    @NotNull
    private ProductType type; // Android / IOS
    @NotNull
    private Double price;
    private Double oldPrice;
    @NotNull
    private Integer amount;
    @NotNull
    private Boolean availability;
    @NotNull
    private Double weight;
    @NotBlank
    private String color;
    @NotNull
    private Double screenDiagonal; // Диагональ экрана
    @NotNull
    private Boolean isFrontCameraExists; // Фронтальная фотокамера
    @NotNull
    private Double frontCameraMp; // Главная фотокамера
    @NotNull
    private Integer simCardAmount; // Количество SIM-карт
    @NotNull
    private Double batteryCapacity; // Емкость аккумулятора
    @NotNull
    private Integer manufactureId; //  ishlab chiqaruvchi
    private List<ImageDTO> imageList;
    private LocalDateTime createdDate;

}
