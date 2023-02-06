package pharmacyinformationsystem.service;

import pharmacyinformationsystem.model.PharmacyMedicinePrice;
import pharmacyinformationsystem.model.PharmedActionPrice;

public interface PharmedPriceService extends GenericService<PharmacyMedicinePrice> {
    public PharmedActionPrice save(PharmedActionPrice entity);
}
