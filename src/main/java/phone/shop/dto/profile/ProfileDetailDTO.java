package phone.shop.dto.profile;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import phone.shop.types.ProfileRole;
import phone.shop.types.ProfileStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProfileDetailDTO {
    private Integer id;
    private String name;
    private String surname;
    private String email;
    private String contact;
    private ProfileStatus status;
    private String password;
    private ProfileRole role;
    private LocalDateTime createdDate;
    private String token;

}
