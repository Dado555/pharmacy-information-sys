package pharmacyinformationsystem.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import pharmacyinformationsystem.model.Appointment;
import pharmacyinformationsystem.model.Pharmacy;
import pharmacyinformationsystem.model.Point;
import pharmacyinformationsystem.model.enums.AppointmentStatus;
import pharmacyinformationsystem.model.users.*;
import pharmacyinformationsystem.repository.AppointmentRepository;
import pharmacyinformationsystem.repository.ConfirmationTokenRepository;
import pharmacyinformationsystem.service.impl.JpaAppointmentService;
import pharmacyinformationsystem.web.dto.domain.AppointmentDTO;
import pharmacyinformationsystem.web.exception.BadRequestException;

import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static pharmacyinformationsystem.constants.AppointmentConstants.APPOINTMENT_ID;
import static pharmacyinformationsystem.constants.AppointmentConstants.getAppointmentExample;
import static pharmacyinformationsystem.constants.PharmacyConstants.PHARMACY_ID;
import static pharmacyinformationsystem.constants.UserConstants.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AppointmentServiceTest {

    @Mock
    private AppointmentRepository appointmentRepositoryMock;
    @Mock
    private PatientService patientServiceMock;
    @Mock
    private PharmacyService pharmacyServiceMock;
    @Mock
    private PharmacistService pharmacistServiceMock;
    @Mock
    private ConfirmationTokenRepository confirmationTokenRepositoryMock;

    @InjectMocks
    private JpaAppointmentService appointmentService;

    @Test(expected = BadRequestException.class)
    public void testScheduleAppointmentCounselingPenals() {
        Patient patient = new Patient(); patient.setId(PATIENT_ID);
        patient.setPenalties(Arrays.asList(new Penalty(), new Penalty(), new Penalty()));

        Pharmacy pharmacy = new Pharmacy(); pharmacy.setId(PHARMACY_ID);
        Pharmacist pharmacist = new Pharmacist(); pharmacist.setId(PHARMACIST_ID);

        AppointmentDTO appointmentDTO = new AppointmentDTO();
        appointmentDTO.setPatientId(PATIENT_ID);
        appointmentDTO.setPharmacyId(PHARMACY_ID);
        appointmentDTO.setWorkerId(PHARMACIST_ID);
        appointmentDTO.setStart(1624998025000L);
        appointmentDTO.setEnd(1624998026000L);

        when(patientServiceMock.findOneWithLock(PATIENT_ID)).thenReturn(patient);
        when(pharmacyServiceMock.findOne(PHARMACY_ID)).thenReturn(pharmacy);
        when(pharmacistServiceMock.findOne(PHARMACIST_ID)).thenReturn(pharmacist);

        appointmentService.scheduleAppointmentCounseling(PATIENT_ID, appointmentDTO);
    }

    @Test
    public void testScheduleAppointmentCounseling() {
        Patient patient = new Patient(); patient.setId(PATIENT_ID); patient.setEmail("pera@email.com"); patient.setPoints(0.0);

        Point point = new Point();
        point.setPharmacistCounseling(10.0);
        Pharmacy pharmacy = new Pharmacy(); pharmacy.setId(PHARMACY_ID); pharmacy.setPoint(point);
        Pharmacist pharmacist = new Pharmacist(); pharmacist.setId(PHARMACIST_ID); pharmacist.setActive(true);

        AppointmentDTO appointmentDTO = new AppointmentDTO();
        appointmentDTO.setPatientId(PATIENT_ID);
        appointmentDTO.setPharmacyId(PHARMACY_ID);
        appointmentDTO.setWorkerId(PHARMACIST_ID);
        appointmentDTO.setStart(1632914425000L);
        appointmentDTO.setEnd(1632914426000L);

        when(patientServiceMock.findOneWithLock(PATIENT_ID)).thenReturn(patient);
        when(pharmacyServiceMock.findOne(PHARMACY_ID)).thenReturn(pharmacy);
        when(pharmacistServiceMock.findOneWithLock(PHARMACIST_ID)).thenReturn(pharmacist);
        when(pharmacyServiceMock.findPharmacistsByDateAndTimeCriteria(appointmentDTO.getPharmacyId(), appointmentDTO.getStart(), appointmentDTO.getEnd() - appointmentDTO.getStart())).thenReturn(Collections.singletonList(pharmacist));

        Appointment appointment = appointmentService.scheduleAppointmentCounseling(PATIENT_ID, appointmentDTO);

        assertThat(patient.getAppointments()).hasSize(1);
        assertThat(appointment.getPatient().getId()).isEqualTo(PATIENT_ID);
        assertThat(appointment.getAppointmentStatus()).isEqualTo(AppointmentStatus.RESERVED);

        verify(patientServiceMock, times(1)).findOneWithLock(PATIENT_ID);
        verify(pharmacyServiceMock, times(1)).findOne(PHARMACY_ID);
        verify(pharmacistServiceMock, times(1)).findOneWithLock(PHARMACIST_ID);
    }

    @Test(expected = BadRequestException.class)
    public void testScheduleAppointmentExaminationPenals() {
        Patient patient = new Patient(); patient.setId(PATIENT_ID);
        patient.setPenalties(Arrays.asList(new Penalty(), new Penalty(), new Penalty()));

        Appointment appointment = getAppointmentExample();

        when(patientServiceMock.findOne(PATIENT_ID)).thenReturn(patient);
        when(appointmentRepositoryMock.findById(APPOINTMENT_ID)).thenReturn(java.util.Optional.of(appointment));

        appointmentService.scheduleAppointmentExamination(PATIENT_ID, APPOINTMENT_ID);

        verify(patientServiceMock, times(1)).findOne(PATIENT_ID);
    }

    @Test
    public void testScheduleAppointmentExamination() {
        Patient patient = new Patient(); patient.setId(PATIENT_ID); patient.setEmail("asdas");

        Appointment appointment = getAppointmentExample();

        when(patientServiceMock.findOne(PATIENT_ID)).thenReturn(patient);
        when(appointmentRepositoryMock.findById(APPOINTMENT_ID)).thenReturn(java.util.Optional.of(appointment));

        appointmentService.scheduleAppointmentExamination(PATIENT_ID, APPOINTMENT_ID);

        assertThat(patient.getAppointments()).hasSize(1);
        assertThat(appointment.getPatient().getId()).isEqualTo(PATIENT_ID);
        assertThat(appointment.getAppointmentStatus()).isEqualTo(AppointmentStatus.RESERVED);

        verify(patientServiceMock, times(1)).findOne(PATIENT_ID);
        verify(appointmentRepositoryMock, times(1)).findById(APPOINTMENT_ID);
    }
}
