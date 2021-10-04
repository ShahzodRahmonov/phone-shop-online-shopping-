package phone.shop.converter;

import phone.shop.dto.CommentDTO;
import phone.shop.entity.CommentEntity;

public class CommentConverter {
    public static CommentDTO toDTO(CommentEntity entity) {
        CommentDTO dto = new CommentDTO();
        dto.setId(entity.getId());
        dto.setContent(entity.getContent());
        dto.setContent(entity.getContent());
        dto.setContent(entity.getContent());
        dto.setContent(entity.getContent());

        dto.setId(entity.getId());

        dto.setCreatedDate(entity.getCreatedDate());

        return dto;
    }

    public CommentConverter() {
    }


    @Override
    public String toString() {
        return "CommentConverter{}";
    }


}
