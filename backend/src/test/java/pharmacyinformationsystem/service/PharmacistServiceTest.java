package pharmacyinformationsystem.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import pharmacyinformationsystem.model.Appointment;
import pharmacyinformationsystem.model.Pharmacy;
import pharmacyinformationsystem.model.enums.AppointmentType;
import pharmacyinformationsystem.model.users.Dermatologist;
import pharmacyinformationsystem.model.users.Pharmacist;
import pharmacyinformationsystem.repository.AppointmentRepository;
import pharmacyinformationsystem.repository.DermatologistRepository;
import pharmacyinformationsystem.repository.PharmacistRepository;
import pharmacyinformationsystem.repository.PharmacyRepository;
import pharmacyinformationsystem.service.impl.JpaDermatologistService;
import pharmacyinformationsystem.service.impl.JpaPharmacistService;
import pharmacyinformationsystem.web.dto.domain.AppointmentDTO;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PharmacistServiceTest {
    @Mock
    private PharmacistRepository pharmacistRepository;

    @Mock
    private DermatologistRepository dermatologistRepository;

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private PharmacyRepository pharmacyRepository;

    @InjectMocks
    private JpaDermatologistService dermatologistService;

    @InjectMocks
    private JpaPharmacistService pharmacistService;

    @Test
    @Transactional
    public void testCreateAppointment()
    {
        AppointmentDTO a = new AppointmentDTO();
        a.setPatientId(32);
        a.setWorkerId(17);
        a.setPharmacyId(2);
        a.setTitle(AppointmentType.COUNSELING);
        a.setPrice(369.0);
        a.setStart(Long.parseLong("1627638600000"));
        a.setEnd(Long.parseLong("1627642200000"));

        Pharmacist pharmacist = new Pharmacist();
        pharmacist.setId(a.getWorkerId());

        when(pharmacistService.findOne(a.getWorkerId())).thenReturn(pharmacist);

        List<Appointment> appointments = new ArrayList<>();
        when(appointmentRepository.findByMedicalWorkerId(a.getWorkerId())).thenReturn(appointments);

        Pharmacy pharmacy = new Pharmacy();
        pharmacy.setId(a.getPharmacyId());
        when(pharmacyRepository.getOne(a.getPharmacyId())).thenReturn(pharmacy);

        Boolean isCreated = pharmacistService.createAppointment(a);
        assertThat(isCreated).isEqualTo(true);
    }

    @Test
    @Transactional
    public void testCreateAppointmentDer()
    {
        AppointmentDTO a = new AppointmentDTO();
        a.setPatientId(32);
        a.setWorkerId(11);
        a.setPharmacyId(1);
        a.setTitle(AppointmentType.EXAMINATION);
        a.setPrice(369.0);
        a.setStart(Long.parseLong("1627638600000"));
        a.setEnd(Long.parseLong("1627642200000"));

        Dermatologist dermatologist = new Dermatologist();
        dermatologist.setId(a.getWorkerId());

        when(dermatologistService.findOne(a.getWorkerId())).thenReturn(dermatologist);

        List<Appointment> appointments = new ArrayList<>();
        when(appointmentRepository.findByMedicalWorkerId(a.getWorkerId())).thenReturn(appointments);

        Pharmacy pharmacy = new Pharmacy();
        pharmacy.setId(a.getPharmacyId());
        when(pharmacyRepository.getOne(a.getPharmacyId())).thenReturn(pharmacy);

        Boolean isCreated = dermatologistService.createAppointment(a);
        assertThat(isCreated).isEqualTo(true);
    }
}
