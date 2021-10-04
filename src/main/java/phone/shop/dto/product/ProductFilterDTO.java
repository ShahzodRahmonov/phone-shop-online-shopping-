package phone.shop.dto.product;

import lombok.Getter;
import lombok.Setter;
import phone.shop.dto.FilterDTO;
import phone.shop.dto.ManufacturerDTO;
import phone.shop.types.ProductType;

@Getter
@Setter
public class ProductFilterDTO extends FilterDTO {
    private String name;
    private String model;
    private ProductType type; // Android / IOS

    private Double minPrice;
    private Double maxPrice;

    private Double minWeight;
    private Double maxWeight;

    private String color;

    private Double fromScreenDiagonal; // Диагональ экрана
    private Double tosScreenDiagonal; // Диагональ экрана

    private Boolean isFrontCameraExists; // Фронтальная фотокамера
    private Double frontCameraMp; // Главная фотокамера
    private Integer simCardAmount; // Количество SIM-карт
    private Double batteryCapacity; // Емкость аккумулятора
    private Integer manufacturerId; //  ishlab chiqaruvchi
}
