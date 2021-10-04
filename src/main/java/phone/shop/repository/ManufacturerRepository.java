package phone.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import phone.shop.entity.ManufacturerEntity;

public interface ManufacturerRepository extends JpaRepository<ManufacturerEntity, Integer> {
}
