package phone.shop.service;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Service;
import phone.shop.dto.AuthorizationDTO;
import phone.shop.dto.RegistrationDTO;
import phone.shop.dto.profile.ProfileDetailDTO;
import phone.shop.entity.ProfileEntity;
import phone.shop.exp.ItemNotFoundException;
import phone.shop.exp.ProfileNotFoundException;
import phone.shop.exp.ServerBadRequestException;
import phone.shop.repository.ProfileRepository;
import phone.shop.types.ProfileRole;
import phone.shop.types.ProfileStatus;
import phone.shop.util.JwtTokenUtil;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Optional;

@Service
public class AuthService {
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private MailSenderService mailSenderService;
    @Autowired
    private ResourceBundleMessageSource messageSource;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Value("${server.url}")
    private String serverUrl;

    public ProfileDetailDTO auth(AuthorizationDTO dto, String lang) {
        String email = dto.getEmail(); // ali@mail.ru
        String pswd = DigestUtils.md5Hex(dto.getPassword()); // abcd123
        Optional<ProfileEntity> optional = this.profileRepository.findByEmailAndPassword(email, pswd);

        if (!optional.isPresent()) {
            throw new ProfileNotFoundException(messageSource.getMessage("email.paswd.incorrect", null, new Locale(lang)));
        }

        ProfileEntity profileEntity = optional.get();
        if (!profileEntity.getStatus().equals(ProfileStatus.ACTIVE)) {
            throw new ProfileNotFoundException(messageSource.getMessage("not.active", null, new Locale(lang)));
        }

        String jwt = jwtTokenUtil.generateAccessToken(profileEntity.getId(), profileEntity.getEmail());

        ProfileDetailDTO responseDTO = new ProfileDetailDTO();
        responseDTO.setToken(jwt);
        responseDTO.setName(profileEntity.getName());
        responseDTO.setSurname(profileEntity.getSurname());
        responseDTO.setContact(profileEntity.getContact());

        return responseDTO;
    }

    public String registration(RegistrationDTO dto) {
        Optional<ProfileEntity> optional = profileRepository.getByEmail(dto.getEmail());
        if (optional.isPresent()) {
            throw new ServerBadRequestException("Email already exists.");
        }

        ProfileEntity entity = new ProfileEntity();
        entity.setName(dto.getName());
        entity.setSurname(dto.getSurname());
        entity.setEmail(dto.getEmail());
        entity.setContact(dto.getContact());
        entity.setRole(ProfileRole.ROLE_USER);
        entity.setStatus(ProfileStatus.INACTIVE);
        entity.setCreatedDate(LocalDateTime.now());
        entity.setPassword(DigestUtils.md5Hex(dto.getPassword()));

        this.profileRepository.save(entity);
        String jwt = jwtTokenUtil.generateAccessToken(entity.getId(), entity.getEmail());
        String link = serverUrl + "/auth/verification/" + jwt;
        try {
            mailSenderService.sendEmail(dto.getEmail(),
                    "verification code",
                    "Salom shu linkni bosing." + link);
        } catch (Exception e) {
            this.profileRepository.delete(entity);
        }

        return "Sizning emailingizga verificatsiyadan o'tish linki yuborildi, link orqali verifikatsiyadan o'ting";
    }

    public String verification(String jwt) {
        Integer profileId = Integer.parseInt(jwtTokenUtil.getUserId(jwt));

        Optional<ProfileEntity> optional = this.profileRepository.findById(profileId);
        if (!optional.isPresent()) {
            throw new ItemNotFoundException("Wrong key");
        }
        ProfileEntity profileEntity = optional.get();
        if (!profileEntity.getStatus().equals(ProfileStatus.INACTIVE)) {
            throw new ServerBadRequestException("You are in wrong status");
        }

        profileEntity.setStatus(ProfileStatus.ACTIVE);
        this.profileRepository.save(profileEntity);
        return "Successfully verified";
    }
}
