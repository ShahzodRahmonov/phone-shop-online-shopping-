package phone.shop.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import phone.shop.config.CustomUserDetails;
import phone.shop.dto.ManufacturerDTO;
import phone.shop.service.ManufacturerService;
import phone.shop.types.ProfileRole;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.nio.file.attribute.UserPrincipal;
@Slf4j
@RestController
@RequestMapping("/manufacture")
public class ManufactureController {
    @Autowired
    private ManufacturerService manufacturerService;

    // Admin
    @PostMapping("/action")
    public ResponseEntity<?> create(@Valid @RequestBody ManufacturerDTO dto) {
        log.info("Rest for manufacture create");
        ManufacturerDTO result = manufacturerService.create(dto);
        log.info("Request Done");
        return ResponseEntity.ok().body(result);
    }

    @PutMapping("/action/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Integer id, @Valid @RequestBody ManufacturerDTO dto, HttpServletRequest request) {
//        UserDetails userDetails = TokenUtil.getCurrentUser(request, ProfileRole.ADMIN);
        manufacturerService.update(id, dto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/action/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") Integer id, HttpServletRequest request) {
        ManufacturerDTO dto = manufacturerService.getById(id);
        return ResponseEntity.ok().body(dto);
    }

    @GetMapping("/action/filter")
    public ResponseEntity<?> filter(@RequestParam(value = "page", defaultValue = "0") int page,
                                    @RequestParam(value = "size", defaultValue = "10") int size,
                                    HttpServletRequest request) {
        Page<ManufacturerDTO> dtoList = manufacturerService.list(page, size);
        return ResponseEntity.ok().body(dtoList);
    }
}
