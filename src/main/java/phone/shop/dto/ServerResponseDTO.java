package phone.shop.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ServerResponseDTO {
    private String message;
    private String code;



    public ServerResponseDTO(String message, String code) {
        this.message = message;
        this.code = code;
    }
}
