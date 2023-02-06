package pharmacyinformationsystem.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import pharmacyinformationsystem.model.enums.AppointmentType;
import pharmacyinformationsystem.util.TestUtil;
import pharmacyinformationsystem.web.dto.domain.AppointmentDTO;

import javax.transaction.Transactional;
import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WithMockUser(username="pera@maildrop.cc", roles={"PATIENT"})
@SpringBootTest
public class AppointmentController {

    private static final String URL_PREFIX = "/api/appointments";

    private final MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), StandardCharsets.UTF_8);

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setup() { this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build(); }

    @Test
    @Transactional
    @Rollback
    public void testScheduleAppointmentCounselingNotFoundPharmacy() throws Exception {
        AppointmentDTO a = new AppointmentDTO();
        a.setPatientId(32);
        a.setWorkerId(17);
        a.setPharmacyId(420);
        a.setTitle(AppointmentType.COUNSELING);
        a.setStart(Long.parseLong("1627638600000"));    /* 30.07 11:50 */
        a.setEnd(Long.parseLong("1627642200000"));      /* 30.07 12:50 */

        String json = TestUtil.json(a);
        this.mockMvc.perform(post(URL_PREFIX + "/scheduled/counseling").contentType(contentType).content(json)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    @Rollback
    public void testScheduleAppointmentCounselingNotFoundPharmacist() throws Exception {
        AppointmentDTO a = new AppointmentDTO();
        a.setPatientId(32);
        a.setWorkerId(420);
        a.setPharmacyId(2);
        a.setTitle(AppointmentType.COUNSELING);
        a.setStart(Long.parseLong("1627638600000"));    /* 30.07 11:50 */
        a.setEnd(Long.parseLong("1627642200000"));      /* 30.07 12:50 */

        String json = TestUtil.json(a);
        this.mockMvc.perform(post(URL_PREFIX + "/scheduled/counseling").contentType(contentType).content(json)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    @Rollback
    public void testScheduleAppointmentCounselingBadRequestDateTime() throws Exception {
        AppointmentDTO a = new AppointmentDTO();
        a.setPatientId(32);
        a.setWorkerId(420);
        a.setPharmacyId(2);
        a.setTitle(AppointmentType.COUNSELING);
        a.setStart(Long.parseLong("1609516121000"));    /* 01.01.  */
        a.setEnd(Long.parseLong("1609516121000"));      /* 01.01.  */

        String json = TestUtil.json(a);
        this.mockMvc.perform(post(URL_PREFIX + "/scheduled/counseling").contentType(contentType).content(json)).andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    @Rollback
    public void testScheduleAppointmentCounselingOk() throws Exception {
        AppointmentDTO a = new AppointmentDTO();
        a.setPatientId(32);
        a.setWorkerId(17);
        a.setPharmacyId(1);
        a.setTitle(AppointmentType.COUNSELING);
        a.setStart(Long.parseLong("1627638600000"));    /* 30.07 11:50 */
        a.setEnd(Long.parseLong("1627642200000"));      /* 30.07 12:50 */
        a.setPrice(500D);

        String json = TestUtil.json(a);
        this.mockMvc.perform(post(URL_PREFIX + "/scheduled/counseling").contentType(contentType).content(json)).andExpect(status().isOk());
    }

    @Test
    @Transactional
    @Rollback
    public void testCancelAppointmentCounselingNotFoundAppointment() throws Exception {
        this.mockMvc.perform(put(URL_PREFIX + "/scheduled/counseling/420/cancel")).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    @Rollback
    public void testCancelAppointmentCounselingOk() throws Exception {
        this.mockMvc.perform(put(URL_PREFIX + "/scheduled/counseling/12/cancel")).andExpect(status().isOk());
    }

    @Test
    @Transactional
    @Rollback
    public void testScheduleAppointmentExaminationNotFoundAppointment() throws Exception {
        this.mockMvc.perform(post(URL_PREFIX + "/scheduled/examination").param("id", "420").contentType(contentType)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    @Rollback
    public void testScheduleAppointmentExaminationOk() throws Exception {
        this.mockMvc.perform(post(URL_PREFIX + "/scheduled/examination").param("id", "2").contentType(contentType)).andExpect(status().isOk());
    }

    @Test
    @Transactional
    @Rollback
    public void testCancelAppointmentExaminationNotFoundAppointment() throws Exception {
        this.mockMvc.perform(put(URL_PREFIX + "/scheduled/examination/420/cancel")).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    @Rollback
    public void testCancelAppointmentExaminationOk() throws Exception {
        this.mockMvc.perform(put(URL_PREFIX + "/scheduled/examination/11/cancel")).andExpect(status().isOk());
    }

}
