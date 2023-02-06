package pharmacyinformationsystem.constants;

import pharmacyinformationsystem.model.Medicine;
import pharmacyinformationsystem.model.Pharmacy;
import pharmacyinformationsystem.model.PharmacyMedicine;

import static pharmacyinformationsystem.constants.MedicineConstants.getMedicineExample1;
import static pharmacyinformationsystem.constants.MedicineConstants.getMedicineExample2;
import static pharmacyinformationsystem.constants.PharmacyConstants.getPharmacyExample;

public class PharmacyMedicineConstants {

    public static final Integer PHARM_MED1_ID = 1;
    public static final Integer MEDICINE1_AVAILABLE_AMOUNT = 5;
    public static final Double MEDICINE1_PRICE = 100D;

    public static final Integer PHARM_MED2_ID = 2;
    public static final Integer MEDICINE2_AVAILABLE_AMOUNT = 8;
    public static final Double MEDICINE2_PRICE = 100D;

    public static PharmacyMedicine getPharmacyMedicineExample1() {
        Pharmacy pharmacy = getPharmacyExample();
        Medicine medicine1 = getMedicineExample1();
        PharmacyMedicine pharmacyMedicine = PharmacyMedicine.builder().pharmacy(pharmacy).medicine(medicine1).availableAmount(MEDICINE1_AVAILABLE_AMOUNT).price(MEDICINE1_PRICE).build();
        pharmacyMedicine.setId(PHARM_MED1_ID);
        return pharmacyMedicine;
    }

    public static PharmacyMedicine getPharmacyMedicineExample2() {
        Pharmacy pharmacy = getPharmacyExample();
        Medicine medicine2 = getMedicineExample2();
        PharmacyMedicine pharmacyMedicine = PharmacyMedicine.builder().pharmacy(pharmacy).medicine(medicine2).availableAmount(MEDICINE2_AVAILABLE_AMOUNT).price(MEDICINE2_PRICE).build();
        pharmacyMedicine.setId(PHARM_MED2_ID);
        return pharmacyMedicine;
    }
}
