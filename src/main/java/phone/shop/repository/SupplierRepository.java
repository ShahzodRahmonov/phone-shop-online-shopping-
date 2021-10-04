package phone.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import phone.shop.entity.SupplierEntity;

import javax.transaction.Transactional;

public interface SupplierRepository extends JpaRepository<SupplierEntity, Integer> {
    @Transactional
    @Modifying
    @Query("update SupplierEntity set visible =:visible where id =:productId")
    int changeVisibility(@Param("productId") Integer productId, @Param("visible") Boolean visibility);
}
