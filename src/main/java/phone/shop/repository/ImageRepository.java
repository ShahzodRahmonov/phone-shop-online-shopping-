package phone.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import phone.shop.entity.ImageEntity;

import java.util.Optional;

public interface ImageRepository extends JpaRepository<ImageEntity, Integer> {
    Optional<ImageEntity> findByToken(String token);
}
