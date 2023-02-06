package pharmacyinformationsystem.service;

import pharmacyinformationsystem.model.MedicineReservation;
import pharmacyinformationsystem.model.MedicineReservationItem;

import java.util.List;

public interface MedicineReservationService extends GenericService<MedicineReservation> {

    void deleteById(Integer id);

    List<MedicineReservationItem> getReservationItems(Integer reservationId, Integer pharmacistId);

    MedicineReservationItem issueReservationItem(Integer reservationItemId);

    List<MedicineReservationItem> getMedicineReservationItems(Integer reservationId);

   void sendIssueConfirmationEmail(MedicineReservationItem mri, Double points);
}
