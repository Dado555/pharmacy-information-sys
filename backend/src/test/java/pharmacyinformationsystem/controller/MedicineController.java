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
import pharmacyinformationsystem.model.Medicine;
import pharmacyinformationsystem.model.enums.MedicineForm;
import pharmacyinformationsystem.model.enums.MedicineIssuingType;
import pharmacyinformationsystem.util.TestUtil;

import javax.transaction.Transactional;
import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WithMockUser(username="nikola@yahoo.com", roles={"SYSTEM_ADMIN", "PHARMACY_ADMIN"})
@SpringBootTest
public class MedicineController {
    private static final String URL_PREFIX = "/api/medicines";

    private final MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), StandardCharsets.UTF_8);

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
    public void testCreateNewMedicineValid() throws Exception {
        Medicine m = new Medicine();
        m.setName("Kafetin");
        m.setType("sedativ");
        m.setMedicineForm(MedicineForm.PILL);
        m.setStructure("sastav");
        m.setManufacture("Galenika");
        m.setMedicineIssuingType(MedicineIssuingType.WITHOUT_PRESCRIPTION);
        m.setDailyIntake(2.0);
        m.setContraindications("kontraindikacije");
        m.setRating(0.0);
        m.setPoints(1.0);
        m.setActive(true);

        String json = TestUtil.json(m);
        this.mockMvc.perform(post(URL_PREFIX).contentType(contentType).content(json)).andExpect(status().isOk());
    }

    @Test
    @Transactional
    @Rollback(true)
    public void testRegisterMedicineWrong() throws Exception {
        this.mockMvc.perform(post(URL_PREFIX + "/register-pharmacy=100-medicine=1").contentType(contentType)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    @Rollback(true)
    public void testRegisterMedicineValid() throws Exception {
        this.mockMvc.perform(post(URL_PREFIX + "/register-pharmacy=1-medicine=1").contentType(contentType)).andExpect(status().isOk());
    }
}
