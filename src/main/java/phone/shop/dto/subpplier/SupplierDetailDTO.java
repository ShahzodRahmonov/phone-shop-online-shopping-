package phone.shop.dto.subpplier;

import lombok.Getter;
import lombok.Setter;
import phone.shop.dto.profile.ProfileDetailDTO;

import java.time.LocalDateTime;

@Setter
@Getter
public class SupplierDetailDTO {
    private Integer id;
    private String carModel;
    private String driverLicenceNumber;
    private LocalDateTime createdDate;
    private ProfileDetailDTO profile;

}
