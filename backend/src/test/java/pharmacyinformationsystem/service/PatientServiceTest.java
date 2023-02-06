package pharmacyinformationsystem.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import pharmacyinformationsystem.model.*;
import pharmacyinformationsystem.model.enums.MedicineReservationStatus;
import pharmacyinformationsystem.model.users.Patient;
import pharmacyinformationsystem.model.users.Penalty;
import pharmacyinformationsystem.repository.ConfirmationTokenRepository;
import pharmacyinformationsystem.repository.PatientRepository;
import pharmacyinformationsystem.service.impl.JpaPatientService;
import pharmacyinformationsystem.web.exception.BadRequestException;
import pharmacyinformationsystem.web.util.MailConstants;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static pharmacyinformationsystem.constants.MedicineReservationConstants.*;
import static pharmacyinformationsystem.constants.MedicineReservationItemConstants.RESERVATION_ITEM_AMOUNT;
import static pharmacyinformationsystem.constants.UserConstants.*;
import static pharmacyinformationsystem.constants.PharmacyMedicineConstants.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PatientServiceTest {

    @Mock
    private PatientRepository patientRepositoryMock;
    @Mock
    private MedicineReservationService reservationServiceMock;
    @Mock
    private MailConstants mailConstants;
    @Mock
    private ConfirmationTokenRepository confirmationTokenRepositoryMock;

    @InjectMocks
    private JpaPatientService patientService;

    @Test(expected = BadRequestException.class)
    public void testAddMedicineReservationForPatientNoAvailableAmount() {
        Patient patient = new Patient();
        patient.setId(PATIENT_ID);

        PharmacyMedicine pharmacyMedicine = getPharmacyMedicineExample1();
        MedicineReservationItem item = new MedicineReservationItem();
        item.setPharmacyMedicine(pharmacyMedicine);
        item.setAmount(MEDICINE1_AVAILABLE_AMOUNT + 1);

        MedicineReservation reservation = new MedicineReservation(RESERVATION_DATE);
        reservation.addReservationItem(item);

        when(patientRepositoryMock.findByIdAndActiveTrue(PATIENT_ID)).thenReturn(patient);

        patientService.addMedicineReservationForPatient(PATIENT_ID, reservation);

        verify(patientRepositoryMock, times(1)).findById(PATIENT_ID);
    }

    @Test(expected = BadRequestException.class)
    public void testAddMedicineReservationForPatientWithPenals() {
        Patient patient = new Patient();
        patient.setId(PATIENT_ID);
        patient.setPenalties(Arrays.asList(new Penalty(), new Penalty(), new Penalty()));

        PharmacyMedicine pharmacyMedicine = getPharmacyMedicineExample1();
        MedicineReservationItem item = new MedicineReservationItem();
        item.setPharmacyMedicine(pharmacyMedicine);
        item.setAmount(RESERVATION_ITEM_AMOUNT);

        MedicineReservation reservation = new MedicineReservation(RESERVATION_DATE);
        reservation.addReservationItem(item);

        when(patientRepositoryMock.findByIdAndActiveTrue(PATIENT_ID)).thenReturn(patient);

        patientService.addMedicineReservationForPatient(PATIENT_ID, reservation);

        verify(patientRepositoryMock, times(1)).findById(PATIENT_ID);
    }

    @Test
    public void testAddMedicineReservationForPatient() {
        Patient patient = new Patient();
        patient.setId(PATIENT_ID);
        patient.setEmail("isa.confirmtoken+pacijent2@gmail.com");
        patient.setPatientCategory(new PatientCategory("REGULAR", 0, 0.0, "black"));

        PharmacyMedicine pharmacyMedicine1 = getPharmacyMedicineExample1();
        PharmacyMedicine pharmacyMedicine2 = getPharmacyMedicineExample2();

        MedicineReservationItem item1 = new MedicineReservationItem();
        item1.setPharmacyMedicine(pharmacyMedicine1);
        item1.setAmount(RESERVATION_ITEM_AMOUNT);

        MedicineReservationItem item2 = new MedicineReservationItem();
        item2.setPharmacyMedicine(pharmacyMedicine2);
        item2.setAmount(RESERVATION_ITEM_AMOUNT);

        MedicineReservation reservation = new MedicineReservation(1624868424000L);
        reservation.addReservationItem(item1);
        reservation.addReservationItem(item2);

        when(patientRepositoryMock.findByIdAndActiveTrue(PATIENT_ID)).thenReturn(patient);

        patientService.addMedicineReservationForPatient(PATIENT_ID, reservation);

        assertThat(patient.getMedicineReservationList()).hasSize(1);
        assertThat(pharmacyMedicine1.getAvailableAmount()).isEqualTo(MEDICINE1_AVAILABLE_AMOUNT - RESERVATION_ITEM_AMOUNT);
        assertThat(pharmacyMedicine2.getAvailableAmount()).isEqualTo(MEDICINE2_AVAILABLE_AMOUNT - RESERVATION_ITEM_AMOUNT);

        verify(patientRepositoryMock, times(1)).findByIdAndActiveTrue(PATIENT_ID);
    }

    @Test
    public void testCancelMedicineReservationForPatient() {
        Patient patient = new Patient();
        patient.setId(PATIENT_ID);
        patient.setEmail("isa.confirmtoken+pacijent2@gmail.com");
        patient.setPatientCategory(new PatientCategory("REGULAR", 0, 0.0, "black"));

        PharmacyMedicine pharmacyMedicine1 = getPharmacyMedicineExample1();
        PharmacyMedicine pharmacyMedicine2 = getPharmacyMedicineExample2();

        MedicineReservationItem item1 = new MedicineReservationItem();
        item1.setPharmacyMedicine(pharmacyMedicine1);
        item1.setAmount(RESERVATION_ITEM_AMOUNT);

        MedicineReservationItem item2 = new MedicineReservationItem();
        item2.setPharmacyMedicine(pharmacyMedicine2);
        item2.setAmount(RESERVATION_ITEM_AMOUNT);

        MedicineReservation reservation = new MedicineReservation(RESERVATION_DATE);
        reservation.setId(RESERVATION_ID);
        reservation.addReservationItem(item1);
        reservation.addReservationItem(item2);

        when(patientRepositoryMock.findByIdAndActiveTrue(PATIENT_ID)).thenReturn(patient);
        when(reservationServiceMock.findOne(RESERVATION_ID)).thenReturn(reservation);

        patientService.addMedicineReservationForPatient(PATIENT_ID, reservation);
        assertThat(pharmacyMedicine1.getAvailableAmount()).isEqualTo(MEDICINE1_AVAILABLE_AMOUNT - RESERVATION_ITEM_AMOUNT);
        assertThat(pharmacyMedicine2.getAvailableAmount()).isEqualTo(MEDICINE2_AVAILABLE_AMOUNT - RESERVATION_ITEM_AMOUNT);

        patientService.cancelMedicineReservationForPatient(PATIENT_ID, RESERVATION_ID);
        assertThat(patient.getMedicineReservationList().get(0).getStatus()).isEqualTo(MedicineReservationStatus.CANCELED);
        assertThat(pharmacyMedicine1.getAvailableAmount()).isEqualTo(MEDICINE1_AVAILABLE_AMOUNT);
        assertThat(pharmacyMedicine2.getAvailableAmount()).isEqualTo(MEDICINE2_AVAILABLE_AMOUNT);
    }

}
