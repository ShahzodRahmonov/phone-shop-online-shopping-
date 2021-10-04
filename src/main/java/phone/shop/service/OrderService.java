package phone.shop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import phone.shop.dto.order.*;
import phone.shop.entity.OrderEntity;
import phone.shop.entity.OrderItemEntity;
import phone.shop.exp.ItemNotFoundException;
import phone.shop.exp.ServerBadRequestException;
import phone.shop.repository.OrderItemRepository;
import phone.shop.repository.OrderRepository;
import phone.shop.types.OrderStatus;

import javax.persistence.criteria.Predicate;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ProfileService profileService;
    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private SupplierService supplierService;
    @Autowired
    private ProductService productService;

    public OrderDetailDTO create(Integer profileId, OrderCreateDTO dto) {
        OrderEntity order = new OrderEntity();
        order.setProfileId(profileId);
        order.setRequirement(dto.getRequirement());
        order.setAddress(dto.getAddress());
        order.setContact(dto.getContact());
        order.setPaymentType(dto.getPaymentType());
        order.setStatus(OrderStatus.IN_PROCESS);
        order.setAddress(dto.getAddress());
        order.setCreatedDate(LocalDateTime.now());

        orderRepository.save(order);

        dto.getItemList().forEach(orderItem -> {
            createNewOrderItem(orderItem, order.getId());
        });
        // prepare response
        OrderDetailDTO detailDTO = toDTO(order);
        List<OrderItemEntity> orderItemList = this.orderItemRepository.findAllByOrderId(detailDTO.getId());
        List<OrderItemDetailDTO> orderItemDTOList = orderItemList.stream().map(this::toDTO).collect(Collectors.toList());
        detailDTO.setOrderItemList(orderItemDTOList);

        return detailDTO;
    }

//    public OrderDetailDTO update(Integer profileId, Integer productId, OrderCreateDTO dto) {
//        OrderEntity entity = get(productId);
//
//        if (!entity.getProfileId().equals(profileId)) {
//            throw new ServerBadRequestException("Not Belongs to Profile");
//        }
//        if (!entity.getStatus().equals(OrderStatus.IN_PROCESS)) {
//            throw new ServerBadRequestException("Order In Wrong Status");
//        }
//
//        entity.setProfileId(profileId);
//        entity.setRequirement(dto.getRequirement());
//        entity.setAddress(dto.getAddress());
//        entity.setContact(dto.getContact());
//        entity.setPaymentType(dto.getPaymentType());
//        entity.setAddress(dto.getAddress());
//        entity.setCreatedDate(LocalDateTime.now());
//
//        orderRepository.save(entity);
//
//        List<OrderItemEntity> oldList = orderItemRepository.findAllByOrderId(entity.getId());
//        List<OrderItemCreateDTO> newList = dto.getItemList();
//
//        newList.forEach(orderItemCreateDTO -> { // 2,4
//            if (orderItemCreateDTO.getId() == null) {
//                createNewOrderItem(orderItemCreateDTO, entity.getId());
//            } else {
//                // update
//            }
//        });
//
//        oldList.forEach(orderItemEntity -> { // 2,3
//            if (!newList.stream().anyMatch(newItem -> newItem.getId() != null && orderItemEntity.getId().equals(newItem.getId()))) {
//                // delete
//            }
//        });
//        return null;
//
//    }

    public Page<OrderDetailDTO> filter(OrderFilterDTO filterDTO) {
        String sortBy = filterDTO.getSortBy();
        Sort.Direction direction = filterDTO.getDirection();
        if (sortBy == null || sortBy.isEmpty()) {
            sortBy = "createdDate";
            direction = Sort.Direction.DESC;
        }
        Pageable pageable = PageRequest.of(filterDTO.getPage(), filterDTO.getSize(), direction, sortBy);

        List<Predicate> predicateList = new ArrayList<>();
        Specification<OrderEntity> specification = (root, criteriaQuery, criteriaBuilder) -> {
            if (filterDTO.getOrderDate() != null) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");

//                LocalDateTime time = LocalDateTime.parse(formatter);
                LocalDateTime fromDate = LocalDate.parse(filterDTO.getOrderDate(), formatter).atStartOfDay();
                LocalDateTime toDate = fromDate.plusDays(1);

                predicateList.add(criteriaBuilder.between(root.get("createdDate"), fromDate, toDate));
            }
            if (filterDTO.getSupplierId() != null) {
                predicateList.add(criteriaBuilder.equal(root.get("supplierId"), filterDTO.getSupplierId()));
            }
            if (filterDTO.getProfileId() != null) {
                predicateList.add(criteriaBuilder.equal(root.get("profileId"), filterDTO.getProfileId()));
            }

            return criteriaBuilder.and(predicateList.toArray(new Predicate[0]));
        };

        Page<OrderEntity> paging = this.orderRepository.findAll(specification, pageable); // 100

        Page<OrderDetailDTO> resultPaging = paging.map(productEntity -> { // 100
            OrderDetailDTO dto = toDTO(productEntity);
            return dto;
        });
        return resultPaging;
    }

    public OrderDetailDTO getById(Integer orderId) {
        OrderEntity orderEntity = get(orderId);
        OrderDetailDTO orderDetailDTO = toDTO(orderEntity);

        // set order item list
        List<OrderItemEntity> orderItemList = this.orderItemRepository.findAllByOrderId(orderId);
        List<OrderItemDetailDTO> orderItemDTOList = orderItemList.stream().map(this::toDTO).collect(Collectors.toList());
        orderDetailDTO.setOrderItemList(orderItemDTOList);

        return orderDetailDTO;
    }

    // supplier takes order (orderni suplier yetkazib beraman deb olganda)
    public Boolean addSupplier(Integer orderId, Integer supplierId) {
        OrderEntity orderEntity = get(orderId);
        orderEntity.setSupplierId(supplierId);
        orderEntity.setStatus(OrderStatus.ON_THE_WAY);
        orderRepository.save(orderEntity);
        return true;
    }

    // order yetkazib berilganda supp;ier tomonidan ishlatiladigan metod
    public Boolean orderDelivered(Integer orderId, Integer supplierId) {
        OrderEntity orderEntity = get(orderId);
        if (!orderEntity.getSupplierId().equals(supplierId)) {
            throw new ServerBadRequestException("Wrong Supplier");
        }
        orderEntity.setStatus(OrderStatus.DELIVERED);
        orderEntity.setDeliveredDate(LocalDateTime.now());
        orderRepository.save(orderEntity);
        return true;
    }

    // TODO Get Profile Active order list
    // TODO Get Profile Order history

    public OrderDetailDTO toDTO(OrderEntity orderEntity) {
        OrderDetailDTO order = new OrderDetailDTO();
        order.setId(orderEntity.getId());
        order.setProfile(profileService.getDetail(orderEntity.getProfileId()));
        order.setRequirement(orderEntity.getRequirement());
        order.setAddress(orderEntity.getAddress());
        order.setContact(orderEntity.getContact());
        order.setDeliveryCost(order.getDeliveryCost());
        order.setPaymentType(orderEntity.getPaymentType());
        order.setStatus(orderEntity.getStatus());

        if (orderEntity.getSupplierId() != null) {
            order.setSupplier(supplierService.getById(orderEntity.getSupplierId()));
        }
        order.setCreatedDate(LocalDateTime.now());
        return order;
    }

    public OrderItemDetailDTO toDTO(OrderItemEntity itemEntity) {
        OrderItemDetailDTO dto = new OrderItemDetailDTO();
        dto.setId(itemEntity.getId());
        dto.setCreatedDate(itemEntity.getCreatedDate());
        dto.setAmount(itemEntity.getAmount());
        dto.setOrderId(itemEntity.getOrderId());
        dto.setProduct(productService.getById(itemEntity.getProductId(), true));
        dto.setPrice(itemEntity.getPrice());
        return dto;
    }

    public OrderEntity get(Integer id) {
        return orderRepository.findById(id).orElseThrow(() -> new ItemNotFoundException("Item Not Found"));
    }

    private OrderItemEntity createNewOrderItem(OrderItemCreateDTO dto, Integer orderId) {
        OrderItemEntity orderItemEntity = new OrderItemEntity();
        orderItemEntity.setOrderId(orderId);
        orderItemEntity.setProductId(dto.getProductId());
        orderItemEntity.setAmount(dto.getAmount());
        orderItemEntity.setPrice(productService.get(dto.getProductId()).getPrice()); // get product price
        orderItemEntity.setCreatedDate(LocalDateTime.now());
        orderItemRepository.save(orderItemEntity);
        return orderItemEntity;
    }


}
