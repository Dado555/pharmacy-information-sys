package pharmacyinformationsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pharmacyinformationsystem.model.MedicineReservationItem;

import java.util.List;

@Repository
public interface MedicineReservationItemRepository extends JpaRepository<MedicineReservationItem, Integer> {
    @Query("select res from MedicineReservationItem res where res.issued = false and res.medicineReservation.id = ?1 " +
            "and res.medicineReservation.dateAndTime > ?3 and res.pharmacyMedicine.pharmacy.id = ?2")
    List<MedicineReservationItem> findReservationItemsForPharmacy(Integer reservationId, Integer pharmacyId, Long date);

    MedicineReservationItem findOneById(Integer itemId);
}
