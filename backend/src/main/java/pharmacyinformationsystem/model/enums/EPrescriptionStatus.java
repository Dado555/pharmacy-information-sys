package pharmacyinformationsystem.model.enums;

public enum EPrescriptionStatus {

    NEW(1), PROCESSED(2), REJECTED(3);

    private int ePrescriptionStatus;

    EPrescriptionStatus(int i) {
        this.ePrescriptionStatus = i;
    }

    public int getEPrescriptionStatus() {
        return ePrescriptionStatus;
    }

    public void setEPrescriptionStatus(int ePrescriptionStatus) {
        this.ePrescriptionStatus = ePrescriptionStatus;
    }

}
