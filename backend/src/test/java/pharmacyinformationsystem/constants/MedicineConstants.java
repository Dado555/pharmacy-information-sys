package pharmacyinformationsystem.constants;

import pharmacyinformationsystem.model.Medicine;

public class MedicineConstants {

    public static final Integer MEDICINE1_ID = 1;
    public static final Integer MEDICINE2_ID = 2;

    public static Medicine getMedicineExample1() {
        Medicine medicine = new Medicine();
        medicine.setId(MEDICINE1_ID);
        return medicine;
    }

    public static Medicine getMedicineExample2() {
        Medicine medicine = new Medicine();
        medicine.setId(MEDICINE2_ID);
        return medicine;
    }
}
