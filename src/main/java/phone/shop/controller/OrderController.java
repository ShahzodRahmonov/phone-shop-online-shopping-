package phone.shop.controller;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import phone.shop.dto.order.OrderCreateDTO;
import phone.shop.dto.order.OrderDetailDTO;
import phone.shop.dto.order.OrderFilterDTO;
import phone.shop.service.OrderService;
import phone.shop.util.SpringSecurityUtil;

import javax.validation.Valid;

@RestController
@RequestMapping("/order")
@Api(tags = "Order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping("/action/create")
    public ResponseEntity<OrderDetailDTO> create(@Valid @RequestBody OrderCreateDTO dto) {
        Integer userId = SpringSecurityUtil.getUserId();
        OrderDetailDTO result = orderService.create(userId, dto);
        return ResponseEntity.ok().body(result);
    }

//    @PutMapping("/action/{id}")
//    public ResponseEntity<OrderDetailDTO> update(@Valid @RequestBody OrderCreateDTO dto, @PathVariable("id") Integer productId) {
//        Integer userId = SpringSecurityUtil.getUserId();
//        OrderDetailDTO result = orderService.update(userId, productId, dto);
//        return ResponseEntity.ok().body(result);
//    }

    @GetMapping("/action/{id}")
    public ResponseEntity<OrderDetailDTO> getById(@PathVariable("id") Integer productId) {
        Integer userId = SpringSecurityUtil.getUserId();
        OrderDetailDTO result = orderService.getById(productId);
        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/action/filter")
    public ResponseEntity<Page<OrderDetailDTO>> filter(@RequestBody OrderFilterDTO filterDTO) {
        Integer userId = SpringSecurityUtil.getUserId();
        Page<OrderDetailDTO> result = orderService.filter(filterDTO);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/supplier/{id}")
    public ResponseEntity<Page<OrderDetailDTO>> addSupplier(@PathVariable("id") Integer orderId) {
        Integer supplierId = SpringSecurityUtil.getUserId();
        orderService.addSupplier(orderId, supplierId);
        return ResponseEntity.ok().build();
    }

    // change order status to delivered status
    @GetMapping("/supplier/{id}/delivered")
    public ResponseEntity<Page<OrderDetailDTO>> orderDelivered(@PathVariable("id") Integer orderId) {
        Integer supplierId = SpringSecurityUtil.getUserId();
        orderService.orderDelivered(orderId, supplierId);
        return ResponseEntity.ok().build();
    }

}
