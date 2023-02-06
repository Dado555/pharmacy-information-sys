package pharmacyinformationsystem.constants;

import pharmacyinformationsystem.model.Pharmacy;

public class PharmacyConstants {

    public static final Integer PHARMACY_ID = 1;

    public static Pharmacy getPharmacyExample() {
        Pharmacy pharmacy = new Pharmacy();
        pharmacy.setId(PHARMACY_ID);
        return pharmacy;
    }
}
