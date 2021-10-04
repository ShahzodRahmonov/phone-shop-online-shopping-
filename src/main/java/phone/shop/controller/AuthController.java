package phone.shop.controller;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import phone.shop.dto.AuthorizationDTO;
import phone.shop.dto.RegistrationDTO;
import phone.shop.dto.profile.ProfileDetailDTO;
import phone.shop.entity.ProfileEntity;
import phone.shop.repository.ProfileRepository;
import phone.shop.service.AuthService;
import phone.shop.types.ProfileRole;
import phone.shop.types.ProfileStatus;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/auth")
@Api(tags = "Auth")
public class AuthController {
//    private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthService authService;
    @Autowired
    private ProfileRepository profileRepository;

//    @GetMapping("/test")
//    public String test() {
//        log.trace("A TRACE Message");
//        log.debug("A DEBUG Message");
//        log.info("An INFO Message");
//        log.warn("A WARN Message");
//        log.error("An ERROR Message");
//        return "<h1> API WORKING </h1>";
//    }

//    @GetMapping("/init")
//    public String initAdmin() {
//        log.info("Request for creating admin.");
//        Optional<ProfileEntity> optional = this.profileRepository.getByEmail("admin_email@mail.com");
//        if (optional.isPresent()) {
//            log.info("Admin Already exists");
//            return "<h1> Admin Already exists </h1>";
//        }
//        ProfileEntity entity = new ProfileEntity();
//        entity.setName("Adminjon");
//        entity.setSurname("AdminUser");
//        entity.setEmail("admin_email@mail.com");
//        entity.setContact("123456789");
//        entity.setRole(ProfileRole.ROLE_ADMIN);
//        entity.setStatus(ProfileStatus.ACTIVE);
//        entity.setCreatedDate(LocalDateTime.now());
//        entity.setPassword(DigestUtils.md5Hex("123abc"));
//        this.profileRepository.save(entity);
//        return "<h1> Init Admin done</h1>";
//    }


    @PostMapping("/authorization")
    public ResponseEntity<?> auth(@RequestHeader(value = "Accept-Language", defaultValue = "uz") String lang, @Valid @RequestBody AuthorizationDTO dto) {
        log.info("Authorization: {} " + dto + ". Lang = " + lang);
        ProfileDetailDTO result = this.authService.auth(dto, lang);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/registration")
    public ResponseEntity<?> registration(@Valid @RequestBody RegistrationDTO dto) {
        log.info("Registration: {} " + dto);
        String result = this.authService.registration(dto);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/verification/{jwt}")
    public ResponseEntity<?> registration(@PathVariable("jwt") String jwt) {
        log.info("verification: jwt " + jwt);
        String result = this.authService.verification(jwt);
        return ResponseEntity.ok(result);
    }
}
