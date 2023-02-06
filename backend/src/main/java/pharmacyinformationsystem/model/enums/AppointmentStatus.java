package pharmacyinformationsystem.model.enums;

public enum AppointmentStatus {
    FREE(1), RESERVED(2), CANCELED(3), DONE(4);

    private int appointmentStatus;

    AppointmentStatus(int i) {
        this.appointmentStatus = i;
    }

    public int getAppointmentStatus() {
        return appointmentStatus;
    }

    public void setAppointmentStatus(int appointmentStatus) {
        this.appointmentStatus = appointmentStatus;
    }
}
