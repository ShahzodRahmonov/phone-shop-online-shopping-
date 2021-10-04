package phone.shop.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import phone.shop.entity.CommentEntity;

import java.util.List;

public interface CommentRepository extends JpaRepository<CommentEntity, Integer> {

    @Query("FROM CommentEntity where profile.id =:id")
    List<CommentEntity> findByProfileId(@Param("id") Integer profileId, Pageable pageable);
}
