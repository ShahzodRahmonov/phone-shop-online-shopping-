package phone.shop.service;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import phone.shop.converter.ProfileConverter;
import phone.shop.dto.profile.ProfileCreateDTO;
import phone.shop.dto.profile.ProfileDetailDTO;
import phone.shop.dto.profile.ProfileFilterDTO;
import phone.shop.dto.profile.ProfileUpdateDTO;
import phone.shop.entity.ProfileEntity;
import phone.shop.exp.ItemNotFoundException;
import phone.shop.exp.ServerBadRequestException;
import phone.shop.repository.ProfileRepository;
import phone.shop.types.ProfileRole;
import phone.shop.types.ProfileStatus;

import javax.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class ProfileService {
    @Autowired
    private ProfileRepository profileRepository;

    public ProfileDetailDTO getDetail(Integer profileId) {
        ProfileEntity entity = get(profileId);

        ProfileDetailDTO dto = new ProfileDetailDTO();
        dto.setName(entity.getName());
        dto.setSurname(entity.getSurname());
        dto.setEmail(entity.getEmail());
        dto.setContact(entity.getContact());
        return dto;
    }

    public Boolean profileUpdateDetail(Integer id, ProfileUpdateDTO dto) { // profile update own detail
        ProfileEntity entity = get(id);
        entity.setName(dto.getName());
        entity.setSurname(dto.getSurname());
        entity.setContact(dto.getContact());
        this.profileRepository.save(entity);
        return true;
    }

//    public void profileUpdateEmail(Integer id, String newEmail) {
        // TODO
//    }

    // admin
    public ProfileCreateDTO create(ProfileCreateDTO dto) {

        Optional<ProfileEntity> optional = profileRepository.getByEmail(dto.getEmail());
        if (optional.isPresent()) {
            throw new ServerBadRequestException("Email already exists.");
        }

        if (dto.getRole().equals(ProfileRole.ROLE_USER)) {
            throw new ServerBadRequestException("Sizda foydalanuvchi qo'shish huquqi mavjud emas");
        }
        ProfileEntity entity = new ProfileEntity();
        entity.setName(dto.getName());
        entity.setSurname(dto.getSurname());
        entity.setEmail(dto.getEmail());
        entity.setContact(dto.getContact());
        entity.setRole(dto.getRole());
        entity.setStatus(ProfileStatus.ACTIVE);
        entity.setCreatedDate(LocalDateTime.now());
        entity.setPassword(DigestUtils.md5Hex(dto.getPassword()));
        this.profileRepository.save(entity);

        dto.setId(entity.getId());
        return dto;
    }

    public Boolean update(Integer id, ProfileCreateDTO dto) {
        ProfileEntity entity = get(id);
        entity.setName(dto.getName());
        entity.setSurname(dto.getSurname());
        entity.setEmail(dto.getEmail());
        entity.setContact(dto.getContact());
        entity.setRole(dto.getRole());
        this.profileRepository.save(entity);
        return true;
    }

    public ProfileDetailDTO getById(Integer id) {
        ProfileEntity profileEntity = get(id);
        return ProfileConverter.toDTO(profileEntity);
    }

    public Boolean changeStatus(Integer id, ProfileStatus status) {
        this.profileRepository.updateStatus(status, id);
        return true;
    }

    public Page<ProfileDetailDTO> getListForPagination(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<ProfileEntity> paging = this.profileRepository.findAll(pageable);

        Long totalElements = paging.getTotalElements();
        Integer totalPages = paging.getTotalPages();
        List<ProfileEntity> content = paging.getContent();

        Page<ProfileDetailDTO> resultPage = paging.map(ProfileConverter::toDTO);
        return resultPage;
    }

    public Page<ProfileDetailDTO> filter(ProfileFilterDTO filterDTO) {
        String sortBy = filterDTO.getSortBy();
        if (sortBy == null || sortBy.isEmpty()) {
            sortBy = "createdDate";
        }
        Pageable pageable = PageRequest.of(filterDTO.getPage(), filterDTO.getSize(), filterDTO.getDirection(), sortBy);

        List<Predicate> predicateList = new ArrayList<>();
        Specification<ProfileEntity> specification = (root, criteriaQuery, criteriaBuilder) -> {
            if (filterDTO.getName() != null) {
                predicateList.add(criteriaBuilder.equal(root.get("name"), filterDTO.getName()));
            }
            if (filterDTO.getSurname() != null) {
                predicateList.add(criteriaBuilder.equal(root.get("surname"), filterDTO.getSurname()));
            }
            if (filterDTO.getEmail() != null) {
                predicateList.add(criteriaBuilder.equal(root.get("email"), filterDTO.getEmail()));
            }
            if (filterDTO.getContact() != null) {
                predicateList.add(criteriaBuilder.equal(root.get("contact"), filterDTO.getContact()));
            }
            return criteriaBuilder.and(predicateList.toArray(new Predicate[0]));
        };


        Page<ProfileEntity> paging = this.profileRepository.findAll(specification, pageable);
        List<ProfileDetailDTO> detailDTOList = new LinkedList<>();

        paging.forEach(profileEntity -> {
            detailDTOList.add(ProfileConverter.toDTO(profileEntity));
        });
        return paging.map(ProfileConverter::toDTO);
//        return new PageImpl<>(detailDTOList, pageable, paging.getTotalElements());
    }

    public ProfileEntity get(Integer id) {
        Optional<ProfileEntity> optional = this.profileRepository.findById(id);
        if (!optional.isPresent()) {
            throw new ItemNotFoundException("Profile Not Found");
        }
        return optional.get();
    }


}
