package phone.shop.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Setter
@ToString
public class RegistrationDTO {
    @NotEmpty(message = "Please provide a name")
    private String name;
    @NotBlank(message = "Please provide a surname")
    private String surname;
    @Email(message = "Voy email xatoku")
    private String email;
    @NotEmpty(message = "Please provide a contact")
    private String contact;
    @NotBlank(message = "Please provide a  password")
    @Size(min = 5, max = 8, message = "parol uzunligi 5 dan kam bolmasligi va 8 dan kop bolmasligi kerak")
    private String password;
}
