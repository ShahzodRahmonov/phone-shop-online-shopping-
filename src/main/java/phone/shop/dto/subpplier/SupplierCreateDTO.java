package phone.shop.dto.subpplier;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Setter
@Getter
public class SupplierCreateDTO {
    @NotNull
    private Integer profileId;
    @NotBlank
    private String carModel;
    @NotBlank
    private String driverLicenceNumber;
}
