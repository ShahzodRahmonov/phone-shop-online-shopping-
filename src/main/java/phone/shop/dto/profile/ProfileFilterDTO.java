package phone.shop.dto.profile;

import lombok.Getter;
import lombok.Setter;
import phone.shop.dto.FilterDTO;

import java.util.List;

@Getter
@Setter
public class ProfileFilterDTO  extends FilterDTO {
    private String name;
    private String surname;
    private String email;
    private String contact;
    private List<String> nameList;
}
