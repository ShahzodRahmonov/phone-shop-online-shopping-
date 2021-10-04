package phone.shop.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import phone.shop.dto.ManufacturerDTO;
import phone.shop.entity.ImageEntity;
import phone.shop.entity.ManufacturerEntity;
import phone.shop.exp.ItemNotFoundException;
import phone.shop.repository.ManufacturerRepository;

@Slf4j
@Service
public class ManufacturerService {
    @Autowired
    private ManufacturerRepository manufacturerRepository;
    @Autowired
    private ImageService imageService;


    public ManufacturerDTO create(ManufacturerDTO dto) {
        log.info("Rest for manufacture create: 1");
        ImageEntity imageEntity = imageService.get(dto.getImage().getId());
        ManufacturerEntity entity = new ManufacturerEntity();
        entity.setDescription(dto.getDescription());
        entity.setName(dto.getName());
        entity.setImage(imageEntity);
        manufacturerRepository.save(entity);
        dto.setId(entity.getId());
        dto.setImage(imageService.getImage(imageEntity));
        return dto;
    }

    public Boolean update(Integer id, ManufacturerDTO dto) {
        ManufacturerEntity entity = get(id);
        entity.setDescription(dto.getDescription());
        entity.setName(dto.getName());
        entity.setImage(imageService.get(dto.getImage().getId()));
        manufacturerRepository.save(entity);
        return true;
    }

    public ManufacturerDTO getById(Integer id) {
        return toDTO(get(id));
    }

    public Page<ManufacturerDTO> list(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ManufacturerEntity> paging = manufacturerRepository.findAll(pageable);
        Page<ManufacturerDTO> resultPage = paging.map(this::toDTO);
        return resultPage;
    }

    public ManufacturerEntity get(Integer id) {
        return manufacturerRepository.findById(id).orElseThrow(() -> new ItemNotFoundException("Manufacture Not Found"));
    }

    public ManufacturerDTO toDTO(ManufacturerEntity entity) {
        ManufacturerDTO dto = new ManufacturerDTO();
        dto.setId(entity.getId());
        dto.setName(dto.getName());
        dto.setDescription(entity.getDescription());
        if (entity.getImage() != null)
            dto.setImage(imageService.getImage(entity.getImage().getId()));

        return dto;
    }


}
