package phone.shop.exp;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler({ItemNotFoundException.class})
    public ResponseEntity<?> handlerException(ItemNotFoundException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler({ProfileNotFoundException.class})
    public ResponseEntity<?> handlerException(ProfileNotFoundException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler({MethodNotAllowedException.class})
    public ResponseEntity<?> handlerException(MethodNotAllowedException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler({ServerBadRequestException.class})
    public ResponseEntity<?> handlerException(ServerBadRequestException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
