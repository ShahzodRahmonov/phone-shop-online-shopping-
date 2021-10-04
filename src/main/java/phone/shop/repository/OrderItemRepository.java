package phone.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import phone.shop.entity.OrderEntity;
import phone.shop.entity.OrderItemEntity;
import phone.shop.entity.ProductEntity;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItemEntity, Integer> {

    List<OrderItemEntity> findAllByOrderId(Integer orderId);

}
