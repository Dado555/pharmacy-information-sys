package pharmacyinformationsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pharmacyinformationsystem.model.MedicineReservation;

@Repository
public interface MedicineReservationRepository extends JpaRepository<MedicineReservation, Integer> {

    MedicineReservation findOneById(Integer id);
}
