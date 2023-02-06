package pharmacyinformationsystem.model.enums;

public enum AppointmentType {
    EXAMINATION(1), COUNSELING(2);

    private int appointmentType;

    AppointmentType(int i) {
        this.appointmentType = i;
    }

    public int getAppointmentType() {
        return appointmentType;
    }

    public void setAppointmentType(int appointmentType) {
        this.appointmentType = appointmentType;
    }
}
