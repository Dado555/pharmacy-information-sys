package pharmacyinformationsystem.constants;

import pharmacyinformationsystem.model.Appointment;
import pharmacyinformationsystem.model.Pharmacy;
import pharmacyinformationsystem.model.enums.AppointmentStatus;
import pharmacyinformationsystem.model.enums.AppointmentType;
import pharmacyinformationsystem.model.users.Dermatologist;

import static pharmacyinformationsystem.constants.PharmacyConstants.PHARMACY_ID;
import static pharmacyinformationsystem.constants.UserConstants.DERMATOLOGIST_ID;

public class AppointmentConstants {

    public static Integer APPOINTMENT_ID = 1;
    public static Long START_DATE;
    public static Long END_DATE;
    public static Double PRICE = 100D;
    public static AppointmentStatus APPOINTMENT_STATUS = AppointmentStatus.FREE;

    public static Appointment getAppointmentExample() {
        Pharmacy pharmacy = new Pharmacy(); pharmacy.setId(PHARMACY_ID);
        Dermatologist dermatologist = new Dermatologist(); dermatologist.setId(DERMATOLOGIST_ID);

        Appointment appointment = Appointment.builder().startDateAndTime(START_DATE).endDateAndTime(END_DATE).price(PRICE).appointmentStatus(APPOINTMENT_STATUS).appointmentType(AppointmentType.EXAMINATION).build();
        appointment.setId(APPOINTMENT_ID);
        appointment.setPharmacy(pharmacy);
        appointment.setMedicalWorker(dermatologist);
        return appointment;
    }
}
