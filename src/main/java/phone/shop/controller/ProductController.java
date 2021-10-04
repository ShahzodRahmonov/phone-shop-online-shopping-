package phone.shop.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import phone.shop.dto.product.ProductCreateDTO;
import phone.shop.dto.product.ProductDetailDTO;
import phone.shop.dto.product.ProductFilterDTO;
import phone.shop.service.ProductService;
import phone.shop.types.ProfileRole;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/product")
@Api(tags = "Product")
public class ProductController {
    @Autowired
    private ProductService productService;

    @PostMapping("/action/create")
    @ApiOperation(value = "Create new product", notes = "Productni nomini to'gri yoz", nickname = "getGreeting",
            authorizations = {@Authorization(value = "JWT Token")})
    public ResponseEntity<ProductDetailDTO> create(@Valid @RequestBody ProductCreateDTO dto) {
//        UserDetails userDetails = TokenUtil.getCurrentUser(request, ProfileRole.ADMIN);
        ProductDetailDTO result = productService.create(dto);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/action/{id}")
    @ApiOperation(value = "Get product as admin", authorizations = {@Authorization(value = "JWT Token")})
    public ResponseEntity<ProductDetailDTO> getByIdAsAdmin(@PathVariable("id") Integer id) {
        ProductDetailDTO result = productService.getById(id, true);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("action/block/{id}")
    @ApiOperation(value = "Block product", authorizations = {@Authorization(value = "JWT Token")})
    public ResponseEntity<?> block(@PathVariable("id") Integer id) {
        productService.block(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("action/publish/{id}")
    @ApiOperation(value = "Publish product", authorizations = {@Authorization(value = "JWT Token")})
    public ResponseEntity<Void> publish(@PathVariable("id") Integer id) {
        productService.publish(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Get product as user")
    public ResponseEntity<ProductDetailDTO> getByIdAsUser(@PathVariable("id") Integer id) {
        ProductDetailDTO result = productService.getById(id, false);
        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/filter")
    @ApiOperation(value = "Filter Product")
    public ResponseEntity<Page<ProductDetailDTO>> filter(@RequestBody ProductFilterDTO filterDTO) {
        Page<ProductDetailDTO> result = productService.filter(filterDTO);
        return ResponseEntity.ok().body(result);
    }

}
