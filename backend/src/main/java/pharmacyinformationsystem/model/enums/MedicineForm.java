package pharmacyinformationsystem.model.enums;

public enum MedicineForm {
    POWDER(1), CAPSULE(2), PILL(3), UNGUENT(4), GEL(5), SYRUP(6);

    private int medicineForm;

    MedicineForm(int i) {
        this.medicineForm = i;
    }

    public int getMedicineForm() {
        return medicineForm;
    }

    public void setMedicineForm(int medicineForm) {
        this.medicineForm = medicineForm;
    }
}
