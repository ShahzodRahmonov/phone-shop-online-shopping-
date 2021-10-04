package phone.shop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import phone.shop.converter.CommentConverter;
import phone.shop.dto.CommentDTO;
import phone.shop.entity.CommentEntity;
import phone.shop.exp.ItemNotFoundException;
import phone.shop.repository.CommentRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private ProfileService profileService;
    // TODO
    // create
    // getBy id
    // delete --(owner/admin)
    // update

    // getProductCommentList
    // getProfileCommentList

    // (admin)
    // filter

    // 1- comment + profileDTO(id,name,surname,image) +


    public CommentDTO create(CommentDTO dto, Integer profileId) {


        CommentEntity entity = new CommentEntity();
        entity.setContent(dto.getContent());
//        entity.setProfile(profile);
//        entity.setProduct(article);
//        entity.setOrder();
        entity.setCreatedDate(LocalDateTime.now());

        this.commentRepository.save(entity);
        dto.setId(entity.getId());
        return dto;
    }

    public CommentDTO getById(Integer id) {
        return CommentConverter.toDTO(get(id));
    }

    public Boolean update(Integer id, CommentDTO dto) {
        CommentEntity entity = get(id);
        entity.setContent(dto.getContent());
        this.commentRepository.save(entity);
        return true;
    }

    public List<CommentDTO> list(int page, int size) {
        Pageable paging = PageRequest.of(page, size, Sort.by("createdDate").descending());

        List<CommentEntity> list = this.commentRepository.findAll();
        return list.stream().map(entity -> CommentConverter.toDTO(entity)).collect(Collectors.toList());
    }

    public Boolean delete(Integer id) {
        CommentEntity entity = get(id);
        this.commentRepository.delete(entity);
        return true;
    }

    public List<CommentDTO> getListByArticleId(Integer articleId, int page, int size) {
        Pageable paging = PageRequest.of(page, size, Sort.by("createdDate").descending());

        long pageNumber = paging.getPageNumber();


        return null; // list.stream().map(entity -> CommentConverter.toDTO(entity)).collect(Collectors.toList());
    }

    public List<CommentDTO> getListByProfile(Integer profileId, int page, int size) {
        Pageable paging = PageRequest.of(page, size, Sort.by("createdDate").descending());

        return null;
    }

    public CommentEntity get(Integer id) {
        return this.commentRepository.findById(id).orElseThrow(() -> new ItemNotFoundException("Comment not found id: " + id));
    }
}
