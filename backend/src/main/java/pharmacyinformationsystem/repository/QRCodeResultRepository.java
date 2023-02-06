package pharmacyinformationsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pharmacyinformationsystem.model.QRCodeResult;

public interface QRCodeResultRepository extends JpaRepository<QRCodeResult, Integer> {

}
