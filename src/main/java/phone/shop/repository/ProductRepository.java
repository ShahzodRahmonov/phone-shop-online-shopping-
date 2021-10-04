package phone.shop.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import phone.shop.entity.ProductEntity;

import javax.transaction.Transactional;
import java.beans.Transient;

public interface ProductRepository extends JpaRepository<ProductEntity, Integer>, JpaSpecificationExecutor<ProductEntity> {

    Page<ProductEntity> findAllByVisible(Boolean visible, Specification<ProductEntity> specification, Pageable pageable);

    @Transactional
    @Modifying
    @Query("update ProductEntity set visible =:visible where id =:productId")
    int changeVisibility(@Param("productId") Integer productId, @Param("visible") Boolean visibility);
}
