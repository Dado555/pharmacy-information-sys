package pharmacyinformationsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pharmacyinformationsystem.model.PharmedActionPrice;

public interface PharmedActionPriceRepository extends JpaRepository<PharmedActionPrice, Integer> {
}
