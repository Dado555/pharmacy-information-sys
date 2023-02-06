package pharmacyinformationsystem.model.enums;

public enum MedicineIssuingType {
    WITH_PRESCRIPTION(1), WITHOUT_PRESCRIPTION(2);

    private int medicineIssuingType;

    MedicineIssuingType(int i) {
        this.medicineIssuingType = i;
    }

    public int getMedicineIssuingType() {
        return medicineIssuingType;
    }

    public void setMedicineIssuingType(int medicineIssuingType) {
        this.medicineIssuingType = medicineIssuingType;
    }
}
