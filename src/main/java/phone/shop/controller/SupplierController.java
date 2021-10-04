package phone.shop.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import phone.shop.dto.subpplier.SupplierCreateDTO;
import phone.shop.dto.subpplier.SupplierDetailDTO;
import phone.shop.service.SupplierService;
import phone.shop.types.ProfileRole;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/supplier")
@Api(tags = "Supplier")
public class SupplierController {
    @Autowired
    private SupplierService supplierService;

    @PostMapping("/action")
    @ApiOperation(value = "Create new Supplier", authorizations = {@Authorization(value = "JWT Token")})
    public ResponseEntity<SupplierDetailDTO> create(@Valid @RequestBody SupplierCreateDTO dto, HttpServletRequest request) {
//        UserDetails userDetails = TokenUtil.getCurrentUser(request, ProfileRole.ADMIN);
        SupplierDetailDTO result = supplierService.create(dto);
        return ResponseEntity.ok().body(result);
    }

    @PutMapping("/action/{id}")
    @ApiOperation(value = "Update Supplier", authorizations = {@Authorization(value = "JWT Token")})
    public ResponseEntity<Boolean> update(@PathVariable("id") Integer id,
                                          @Valid @RequestBody SupplierCreateDTO dto, HttpServletRequest request) {
//        UserDetails userDetails = TokenUtil.getCurrentUser(request, ProfileRole.ADMIN);
        Boolean result = supplierService.update(id, dto);
        return ResponseEntity.ok().body(result);
    }

    @DeleteMapping("/action/{id}")
    @ApiOperation(value = "Delete Supplier", authorizations = {@Authorization(value = "JWT Token")})
    public ResponseEntity<Boolean> delete(@PathVariable("id") Integer id, HttpServletRequest request) {
//        UserDetails userDetails = TokenUtil.getCurrentUser(request, ProfileRole.ADMIN);
        Boolean result = supplierService.block(id);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/action/list")
    @ApiOperation(value = "Supplier List", authorizations = {@Authorization(value = "JWT Token")})
    public ResponseEntity<Page<SupplierDetailDTO>> list(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                                        @RequestParam(value = "size", defaultValue = "10") Integer size,
                                                        HttpServletRequest request) {
//        UserDetails userDetails = TokenUtil.getCurrentUser(request, ProfileRole.ADMIN);
        Page<SupplierDetailDTO> result = supplierService.list(page, size);
        return ResponseEntity.ok().body(result);
    }

}
