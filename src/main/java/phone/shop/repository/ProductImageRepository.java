package phone.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import phone.shop.entity.ProductImageEntity;

import java.util.List;

public interface ProductImageRepository extends JpaRepository<ProductImageEntity, Integer> {
    public List<ProductImageEntity> findAllByProductId(Integer productId);
}
