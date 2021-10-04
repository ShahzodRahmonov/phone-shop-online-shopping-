package phone.shop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import phone.shop.dto.product.ProductCreateDTO;
import phone.shop.dto.product.ProductDetailDTO;
import phone.shop.dto.product.ProductFilterDTO;
import phone.shop.entity.ProductEntity;
import phone.shop.entity.ProfileEntity;
import phone.shop.exp.ItemNotFoundException;
import phone.shop.exp.ServerBadRequestException;
import phone.shop.repository.ProductRepository;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {
    @Autowired
    private ManufacturerService manufacturerService;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductImageService productImageService;

    public ProductDetailDTO create(ProductCreateDTO dto) {
        ProductEntity entity = new ProductEntity();
        entity.setAmount(dto.getAmount());
        entity.setAvailability(dto.getAvailability());
        entity.setColor(dto.getColor());
        entity.setBatteryCapacity(dto.getBatteryCapacity());
        entity.setDescriptionUz(dto.getDescriptionUz());
        entity.setDescriptionRu(dto.getDescriptionRu());
        entity.setDescriptionEn(dto.getDescriptionEn());
        entity.setFrontCameraMp(dto.getFrontCameraMp());
        entity.setManufacturer(manufacturerService.get(dto.getManufactureId()));
        entity.setModel(dto.getModel());
        entity.setIsFrontCameraExists(dto.getIsFrontCameraExists());
        entity.setName(dto.getName());
        entity.setOldPrice(dto.getOldPrice());
        entity.setPrice(dto.getPrice());
        entity.setScreenDiagonal(dto.getScreenDiagonal());
        entity.setSimCardAmount(dto.getSimCardAmount());
        entity.setWeight(dto.getWeight());
        entity.setType(dto.getType());
        entity.setCreatedDate(LocalDateTime.now());
        entity.setVisible(true);
        productRepository.save(entity); // save

        if (dto.getImageList() != null) {
            List<Integer> imageIdList = dto.getImageList().stream().map(imageDTO -> imageDTO.getId()).collect(Collectors.toList());
            productImageService.create(imageIdList, entity.getId());
        }

        // send result
        ProductDetailDTO result = toDetailDTO(entity);
        result.setManufacturer(manufacturerService.toDTO(entity.getManufacturer())); // set Manufacturer
        result.setImageList(productImageService.getProductImageDTOList(entity.getId())); // set imageList

        return result;
    }

    public ProductCreateDTO update(Integer productId, ProductCreateDTO dto) {
        ProductEntity entity = get(productId);

        entity.setAmount(dto.getAmount());
        entity.setAvailability(dto.getAvailability());
        entity.setColor(dto.getColor());
        entity.setBatteryCapacity(dto.getBatteryCapacity());
        entity.setDescriptionUz(dto.getDescriptionUz());
        entity.setDescriptionRu(dto.getDescriptionRu());
        entity.setDescriptionEn(dto.getDescriptionEn());
        entity.setFrontCameraMp(dto.getFrontCameraMp());
        entity.setManufacturer(manufacturerService.get(dto.getManufactureId()));
        entity.setModel(dto.getModel());
        entity.setIsFrontCameraExists(dto.getIsFrontCameraExists());
        entity.setName(dto.getName());
        entity.setOldPrice(dto.getOldPrice());
        entity.setPrice(dto.getPrice());
        entity.setScreenDiagonal(dto.getScreenDiagonal());
        entity.setSimCardAmount(dto.getSimCardAmount());
        entity.setWeight(dto.getWeight());
        entity.setType(dto.getType());
        entity.setCreatedDate(LocalDateTime.now());

        productRepository.save(entity);
        return dto;
    }

    public ProductDetailDTO getById(Integer id, Boolean isAdmin) {
        ProductEntity productEntity = get(id);
        if (!isAdmin) {
            if (!productEntity.getVisible()) {
                throw new ServerBadRequestException("Product not Published");
            }
        }
        ProductDetailDTO dto = toDetailDTO(productEntity);
        dto.setManufacturer(manufacturerService.toDTO(productEntity.getManufacturer())); // set Manufacturer
        dto.setImageList(productImageService.getProductImageDTOList(productEntity.getId())); // set imageList

        return dto;
    }

    public Page<ProductDetailDTO> filter(ProductFilterDTO filterDTO) {
        String sortBy = filterDTO.getSortBy();
        Sort.Direction direction = filterDTO.getDirection();
        if (sortBy == null || sortBy.isEmpty()) {
            sortBy = "createdDate";
            direction = Sort.Direction.DESC;
        }
        Pageable pageable = PageRequest.of(filterDTO.getPage(), filterDTO.getSize(), direction, sortBy);

        List<Predicate> predicateList = new ArrayList<>();
        Specification<ProductEntity> specification = (root, criteriaQuery, criteriaBuilder) -> {

            predicateList.add(criteriaBuilder.equal(root.get("visible"), true));

            if (filterDTO.getName() != null) {
                predicateList.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + filterDTO.getName().toLowerCase() + "%"));
            }
            if (filterDTO.getColor() != null) {
                predicateList.add(criteriaBuilder.equal(root.get("color"), filterDTO.getColor()));
            }
            if (filterDTO.getModel() != null) {
                predicateList.add(criteriaBuilder.equal(root.get("model"), filterDTO.getModel()));
            }
            if (filterDTO.getMinPrice() != null && filterDTO.getMaxPrice() != null) {
                predicateList.add(criteriaBuilder.between(root.get("price"), filterDTO.getMinPrice(), filterDTO.getMaxPrice()));
            }
            if (filterDTO.getMinWeight() != null && filterDTO.getMaxWeight() != null) {
                predicateList.add(criteriaBuilder.between(root.get("weight"), filterDTO.getMinWeight(), filterDTO.getMaxWeight()));
            }
            if (filterDTO.getFromScreenDiagonal() != null && filterDTO.getTosScreenDiagonal() != null) {
                predicateList.add(criteriaBuilder.between(root.get("screenDiagonal"), filterDTO.getMinWeight(), filterDTO.getMaxWeight()));
            }
            if (filterDTO.getIsFrontCameraExists() != null) {
                predicateList.add(criteriaBuilder.equal(root.get("isFrontCameraExists"), filterDTO.getIsFrontCameraExists()));
            }
            if (filterDTO.getFrontCameraMp() != null) {
                predicateList.add(criteriaBuilder.equal(root.get("frontCameraMp"), filterDTO.getFrontCameraMp()));
            }
            if (filterDTO.getSimCardAmount() != null) {
                predicateList.add(criteriaBuilder.equal(root.get("simCardAmount"), filterDTO.getSimCardAmount()));
            }
            if (filterDTO.getBatteryCapacity() != null) {
                predicateList.add(criteriaBuilder.equal(root.get("batteryCapacity"), filterDTO.getBatteryCapacity()));
            }
            if (filterDTO.getManufacturerId() != null) {
                Join join = root.join("manufacturer"); // manufacture.id = ?
                predicateList.add(criteriaBuilder.equal(join.get("id"), filterDTO.getManufacturerId()));
            }
            return criteriaBuilder.and(predicateList.toArray(new Predicate[0]));
        };

        Page<ProductEntity> paging = this.productRepository.findAll(specification, pageable); // 100

        //1. List<Integer> productIdList // 100
        //2. List<ImageDTO>  imageList = findAllProductIdIn(productIdList) //  150
        //3.  imageList -> productList

        // Question for optimization 1ta zvachka
        Page<ProductDetailDTO> resultPaging = paging.map(productEntity -> { // 100
            ProductDetailDTO dto = toShortDTO(productEntity);
            dto.setImageList(Collections.singletonList(productImageService.getProductImageDTO(productEntity.getId()))); // set imageList
            return dto;
        });
        return resultPaging;
    }

    public ProductEntity get(Integer id) {
        return productRepository.findById(id).orElseThrow(() -> new ItemNotFoundException("Item Not Found"));
    }

    public void block(Integer productId) {
        productRepository.changeVisibility(productId, false);
    }

    public void publish(Integer productId) {
        productRepository.changeVisibility(productId, true);
    }

    public ProductDetailDTO toShortDTO(ProductEntity entity) {
        ProductDetailDTO dto = new ProductDetailDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescriptionUz(entity.getDescriptionUz());
        dto.setDescriptionRu(entity.getDescriptionRu());
        dto.setDescriptionEn(entity.getDescriptionEn());
        dto.setModel(entity.getModel());
        dto.setPrice(entity.getPrice());
        dto.setOldPrice(entity.getOldPrice());
        return dto;
    }

    public ProductDetailDTO toDetailDTO(ProductEntity entity) {
        ProductDetailDTO dto = new ProductDetailDTO();

        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescriptionUz(entity.getDescriptionUz());
        dto.setDescriptionRu(entity.getDescriptionRu());
        dto.setDescriptionEn(entity.getDescriptionEn());
        dto.setModel(entity.getModel());
        dto.setType(entity.getType());
        dto.setPrice(entity.getPrice());
        dto.setOldPrice(entity.getOldPrice());
        dto.setAmount(entity.getAmount());
        dto.setAvailability(entity.getAvailability());
        dto.setWeight(entity.getWeight());
        dto.setColor(entity.getColor());
        dto.setScreenDiagonal(entity.getScreenDiagonal());
        dto.setIsFrontCameraExists(entity.getIsFrontCameraExists());
        dto.setFrontCameraMp(entity.getFrontCameraMp());
        dto.setSimCardAmount(entity.getSimCardAmount());
        dto.setBatteryCapacity(entity.getBatteryCapacity());

        dto.setCreatedDate(entity.getCreatedDate());
        return dto;
    }
}
