package phone.shop.converter;

import phone.shop.dto.profile.ProfileCreateDTO;
import phone.shop.dto.profile.ProfileDetailDTO;
import phone.shop.entity.ProfileEntity;

public class ProfileConverter {


    public static ProfileDetailDTO toDTO(ProfileEntity entity) {
        if (entity == null)
            return null;

        ProfileDetailDTO dto = new ProfileDetailDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setSurname(entity.getSurname());
        dto.setEmail(entity.getEmail());
        dto.setContact(entity.getContact());
        dto.setStatus(entity.getStatus());
        dto.setRole(entity.getRole());
        dto.setCreatedDate(entity.getCreatedDate());
        return dto;
    }

    public static ProfileDetailDTO toShortDetail(ProfileEntity entity) {
        if (entity == null)
            return null;
        ProfileDetailDTO dto = new ProfileDetailDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setSurname(entity.getSurname());
        dto.setEmail(entity.getEmail());
        dto.setContact(entity.getContact());
        dto.setStatus(entity.getStatus());
        return dto;
    }
}
