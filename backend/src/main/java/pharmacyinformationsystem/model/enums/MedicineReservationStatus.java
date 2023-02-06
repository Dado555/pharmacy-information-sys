package pharmacyinformationsystem.model.enums;

public enum MedicineReservationStatus {

    RESERVED(1), TAKEN(2), CANCELED(3);

    private int status;

    MedicineReservationStatus(int i) {
        this.status = i;
    }

    public int getMedicineReservationStatus() {
        return status;
    }

    public void setMedicineReservationStatus(int status) {
        this.status = status;
    }
}
