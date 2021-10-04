package phone.shop.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Setter
@Getter
public class ManufacturerDTO {
    private Integer id;
    @NotBlank(message = "Name is required")
    private String name;
    @NotBlank(message = "Name is required")
    private String description;
    @NotNull
    private ImageDTO image;
}
