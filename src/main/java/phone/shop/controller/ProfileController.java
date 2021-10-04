package phone.shop.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import phone.shop.dto.profile.*;
import phone.shop.service.ProfileService;
import phone.shop.types.ProfileRole;
import phone.shop.types.ProfileStatus;
import phone.shop.util.SpringSecurityUtil;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/profile")
@Api(tags = "Profile")
public class ProfileController {
    @Autowired
    private ProfileService profileService;

    // User
    @GetMapping("/detail")
    @ApiOperation(value = "Get Profile All Detail", notes = "get greeting", nickname = "getGreeting",
            authorizations = {@Authorization(value = "JWT Token")})
    public ResponseEntity<ProfileDetailDTO> getProfileDetail(HttpServletRequest request) {
        Integer userId = SpringSecurityUtil.getUserId();
        ProfileDetailDTO dto = profileService.getDetail(userId);
        return ResponseEntity.ok().body(dto);
    }

    @PutMapping("/detail")
    public ResponseEntity<?> updateDetail(@Valid @RequestBody ProfileUpdateDTO dto) {
        Integer userId = SpringSecurityUtil.getUserId();
        profileService.profileUpdateDetail(userId, dto);
        return ResponseEntity.ok().build();
    }

//    @PutMapping("/email")
//    public ResponseEntity<?> updateEmail(@RequestParam("email") String email) {
//        Integer userId = SpringSecurityUtil.getUserId();
//        profileService.profileUpdateEmail(userId, email);
//        return ResponseEntity.ok().build();
//    }

    // Admin
    @PostMapping("/adm")
    public ResponseEntity<?> create(@RequestBody ProfileCreateDTO dto) {
        profileService.create(dto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/adm/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Integer id, @RequestBody ProfileCreateDTO dto) {
//        UserDetails userDetails = TokenUtil.getCurrentUser(request, ProfileRole.ADMIN);
        profileService.update(id, dto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/adm/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") Integer id) {
//        UserDetails userDetails = TokenUtil.getCurrentUser(request, ProfileRole.ADMIN);
        ProfileDetailDTO result = profileService.getById(id);
        return ResponseEntity.ok().body(result);
    }

    @PutMapping("/adm/{id}/status")
    public ResponseEntity<?> changeStatus(@PathVariable("id") Integer id, @RequestParam("status") ProfileStatus profileStatus, HttpServletRequest request) {
//        UserDetails userDetails = TokenUtil.getCurrentUser(request, ProfileRole.ADMIN);
        profileService.changeStatus(id, profileStatus);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/adm/filter")
    public ResponseEntity<?> filter(@RequestBody ProfileFilterDTO dto, HttpServletRequest request) {
//        UserDetails userDetails = TokenUtil.getCurrentUser(request, ProfileRole.ADMIN);
        Page<ProfileDetailDTO> result = profileService.filter(dto);
        return ResponseEntity.ok().body(result);
    }
}
