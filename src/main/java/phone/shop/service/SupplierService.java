package phone.shop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import phone.shop.converter.ProfileConverter;
import phone.shop.dto.subpplier.SupplierCreateDTO;
import phone.shop.dto.subpplier.SupplierDetailDTO;
import phone.shop.entity.SupplierEntity;
import phone.shop.exp.ItemNotFoundException;
import phone.shop.repository.OrderItemRepository;
import phone.shop.repository.SupplierRepository;

import java.time.LocalDateTime;

@Service
public class SupplierService {
    @Autowired
    private SupplierRepository supplierRepository;
    @Autowired
    private ProfileService profileService;


    public SupplierDetailDTO create(SupplierCreateDTO dto) {
        SupplierEntity supplierEntity = new SupplierEntity();
        supplierEntity.setProfile(profileService.get(dto.getProfileId()));

        supplierEntity.setCarModel(dto.getCarModel());
        supplierEntity.setDriverLicenceNumber(dto.getDriverLicenceNumber());
        supplierEntity.setCreatedDate(LocalDateTime.now());

        supplierRepository.save(supplierEntity);
        return toDTO(supplierEntity);
    }

    public Boolean update(Integer supplierId, SupplierCreateDTO dto) {
        SupplierEntity entity = get(supplierId);
        entity.setCarModel(dto.getCarModel());
        entity.setDriverLicenceNumber(dto.getDriverLicenceNumber());
        entity.setCreatedDate(LocalDateTime.now());
        supplierRepository.save(entity);
        return true;
    }

    public Boolean block(Integer id) {
        supplierRepository.changeVisibility(id, false);
        return true;
    }

    public Page<SupplierDetailDTO> list(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        return supplierRepository.findAll(pageable).map(entity -> toDTO(entity));
    }

    public SupplierDetailDTO toDTO(SupplierEntity entity) {
        SupplierDetailDTO dto = new SupplierDetailDTO();
        dto.setProfile(ProfileConverter.toDTO(entity.getProfile()));
        dto.setId(entity.getId());
        dto.setCarModel(entity.getCarModel());
        dto.setDriverLicenceNumber(entity.getDriverLicenceNumber());
        dto.setCreatedDate(entity.getCreatedDate());
        return dto;
    }

    public SupplierDetailDTO getById(Integer id) {
        return toDTO(get(id));
    }

    public SupplierEntity get(Integer id) {
        return supplierRepository.findById(id).orElseThrow(() -> new ItemNotFoundException("Item Not Found"));
    }

}
