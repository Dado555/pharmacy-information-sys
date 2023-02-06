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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.transaction.Transactional;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@WithMockUser(username="test-user",roles={"DERMATOLOGIST","PHARMACIST"})
@SpringBootTest
public class AppointmentReportControllerTest {
    private static final String URL_PREFIX = "/api/appointment-reports";

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    @Transactional
    @Rollback(true)
    public void testCreateNewAppointmentValid() throws Exception {
        AppointmentDTO a = new AppointmentDTO();
        a.setPatientId(32);
        a.setWorkerId(17);
        a.setPharmacyId(2);
        a.setTitle(AppointmentType.COUNSELING);
        a.setStart(Long.parseLong("1629100800000"));
        a.setEnd(Long.parseLong("1629102600000"));

        String json = TestUtil.json(a);
        this.mockMvc.perform(post(URL_PREFIX + "/new-appointment").contentType(contentType).content(json)).andExpect(status().isOk());
    }

    @Test
    @Transactional
    @Rollback(true)
    public void testCreateNewAppointmentPatientOverlapping() throws Exception {
        AppointmentDTO a = new AppointmentDTO();
        a.setPatientId(32);
        a.setWorkerId(17);
        a.setPharmacyId(2);
        a.setTitle(AppointmentType.COUNSELING);
        a.setStart(Long.parseLong("1622451900000"));
        a.setEnd(Long.parseLong("1622453400000"));

        String json = TestUtil.json(a);
        this.mockMvc.perform(post(URL_PREFIX + "/new-appointment").contentType(contentType).content(json)).andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    @Rollback(true)
    public void testReserveAppointmentPatientOverlapping() throws Exception {
        Map<String, Integer> map = new HashMap<String, Integer>();
        map.put("patientId", 32);

        String json = TestUtil.json(map);
        this.mockMvc.perform(put(URL_PREFIX + "/appointments/5/reserve").contentType(contentType).content(json)).andExpect(status().isBadRequest());
    }
}
