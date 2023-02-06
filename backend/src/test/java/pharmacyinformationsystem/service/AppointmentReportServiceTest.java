package pharmacyinformationsystem.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import pharmacyinformationsystem.model.Appointment;
import pharmacyinformationsystem.model.Pharmacy;
import pharmacyinformationsystem.model.WorkSchedule;
import pharmacyinformationsystem.model.enums.AppointmentType;
import pharmacyinformationsystem.model.users.Dermatologist;
import pharmacyinformationsystem.repository.WorkScheduleRepository;
import pharmacyinformationsystem.service.impl.JpaAppointmentReportService;
import pharmacyinformationsystem.web.dto.domain.AppointmentDTO;
import pharmacyinformationsystem.web.exception.BadRequestException;
import pharmacyinformationsystem.web.exception.NotFoundException;

import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static pharmacyinformationsystem.constants.PharmacyConstants.PHARMACY_ID;
import static pharmacyinformationsystem.constants.UserConstants.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AppointmentReportServiceTest {

    @Mock
    private WorkScheduleRepository workScheduleRepositoryMock;
    @InjectMocks
    private JpaAppointmentReportService appointmentReportServiceMock;

    @Autowired
    private JpaAppointmentReportService appointmentReportService;

    @Test
    @Transactional
    @Rollback(true)
    public void testCreateNewAppointmentValid() {

        AppointmentDTO a = new AppointmentDTO();
        a.setPatientId(32);
        a.setWorkerId(17);
        a.setPharmacyId(2);
        a.setTitle(AppointmentType.COUNSELING);
        a.setStart(Long.parseLong("1628146800000"));
        a.setEnd(Long.parseLong("1628148600000"));

        Appointment appointment = appointmentReportService.createNewAppointment(a);
        assertThat(appointment.getPatient().getId()).isEqualTo(32);
        assertThat(appointment.getStartDateAndTime()).isEqualTo(Long.parseLong("1628146800000"));
        assertThat(appointment.getEndDateAndTime()).isEqualTo(Long.parseLong("1628148600000"));
    }

    @Test(expected = BadRequestException.class)
    @Transactional
    @Rollback(true)
    public void testCreateNewAppointmentPatientOverlapping() {

        AppointmentDTO a = new AppointmentDTO();
        a.setPatientId(32);
        a.setWorkerId(17);
        a.setPharmacyId(2);
        a.setTitle(AppointmentType.COUNSELING);
        a.setStart(Long.parseLong("1622451900000"));
        a.setEnd(Long.parseLong("1622453400000"));

        appointmentReportService.createNewAppointment(a);
    }

    @Test(expected = BadRequestException.class)
    @Transactional
    @Rollback(true)
    public void testCreateNewAppointmentPharmacistOverlapping() {

        AppointmentDTO a = new AppointmentDTO();
        a.setPatientId(32);
        a.setWorkerId(16);
        a.setPharmacyId(2);
        a.setTitle(AppointmentType.COUNSELING);
        a.setStart(Long.parseLong("1624871700000"));
        a.setEnd(Long.parseLong("1624876200000"));

        appointmentReportService.createNewAppointment(a);
    }

    @Test(expected = BadRequestException.class)
    public void testValidateWorkScheduleInvalidEndTime() {
        WorkSchedule ws = new WorkSchedule();
        ws.setStartTime(Long.parseLong("480"));
        ws.setEndTime(Long.parseLong("720"));

        Pharmacy p = new Pharmacy(); p.setId(PHARMACY_ID);
        Dermatologist d = new Dermatologist(); d.setId(DERMATOLOGIST_ID);

        Appointment appointment = new Appointment();
        appointment.setStartDateAndTime(Long.parseLong("1624871700000"));
        appointment.setEndDateAndTime(Long.parseLong("1624876200000"));
        appointment.setPharmacy(p);
        appointment.setMedicalWorker(d);

        when(workScheduleRepositoryMock.findByPharmacyIdAndMedicalWorkerId(PHARMACY_ID, DERMATOLOGIST_ID)).thenReturn(ws);

        appointmentReportServiceMock.validateWorkSchedule(appointment);
    }

    @Test(expected = BadRequestException.class)
    public void testValidateWorkScheduleInvalidStartTime() {
        WorkSchedule ws = new WorkSchedule();
        ws.setStartTime(Long.parseLong("480"));
        ws.setEndTime(Long.parseLong("720"));

        Pharmacy p = new Pharmacy(); p.setId(PHARMACY_ID);
        Dermatologist d = new Dermatologist(); d.setId(DERMATOLOGIST_ID);

        Appointment appointment = new Appointment();
        appointment.setStartDateAndTime(Long.parseLong("1620190800000"));
        appointment.setEndDateAndTime(Long.parseLong("1620194400000"));
        appointment.setPharmacy(p);
        appointment.setMedicalWorker(d);

        when(workScheduleRepositoryMock.findByPharmacyIdAndMedicalWorkerId(PHARMACY_ID, DERMATOLOGIST_ID)).thenReturn(ws);

        appointmentReportServiceMock.validateWorkSchedule(appointment);
    }

    @Test
    public void testValidateWorkScheduleValid() {
        WorkSchedule ws = new WorkSchedule();
        ws.setStartTime(Long.parseLong("480"));
        ws.setEndTime(Long.parseLong("720"));

        Pharmacy p = new Pharmacy(); p.setId(PHARMACY_ID);
        Dermatologist d = new Dermatologist(); d.setId(DERMATOLOGIST_ID);

        Appointment appointment = new Appointment();
        appointment.setStartDateAndTime(Long.parseLong("1620195000000"));
        appointment.setEndDateAndTime(Long.parseLong("1620196200000"));
        appointment.setPharmacy(p);
        appointment.setMedicalWorker(d);

        when(workScheduleRepositoryMock.findByPharmacyIdAndMedicalWorkerId(PHARMACY_ID, DERMATOLOGIST_ID)).thenReturn(ws);

        appointmentReportServiceMock.validateWorkSchedule(appointment);
        verify(workScheduleRepositoryMock, times(1)).findByPharmacyIdAndMedicalWorkerId(PHARMACY_ID, DERMATOLOGIST_ID);
    }

}
