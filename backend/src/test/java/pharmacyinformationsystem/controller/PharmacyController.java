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
import pharmacyinformationsystem.util.TestUtil;
import pharmacyinformationsystem.web.dto.domain.AddressDTO;
import pharmacyinformationsystem.web.dto.domain.MedicineDTO;
import pharmacyinformationsystem.web.dto.domain.OrderItemDTO;

import javax.transaction.Transactional;
import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WithMockUser(username = "pharmacyAdmin1@gmail.com", roles = {"PHARMACY_ADMIN"})
@SpringBootTest
public class PharmacyController {

    private static final String URL_PREFIX = "/api/pharmacies";

    private final MediaType content = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), StandardCharsets.UTF_8);

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setup()
    {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    @Transactional
    @Rollback
    public void testAddOrderItemValid() throws Exception
    {
        OrderItemDTO orderItemDTO = new OrderItemDTO();
        int orderId = 1;
        MedicineDTO medicineDTO = new MedicineDTO();
        medicineDTO.setId(9);
        orderItemDTO.setMedicineDTO(medicineDTO);
        orderItemDTO.setAmount(100);

        String json = TestUtil.json(orderItemDTO);
        this.mockMvc.perform(put(URL_PREFIX + "/" + orderId +"/new-order-item").contentType(content).content(json)).
                andExpect(status().isOk());
    }

    @Test
    @Transactional
    @Rollback
    public void testAddOrderItemNotFound() throws Exception
    {
        OrderItemDTO orderItemDTO = new OrderItemDTO();
        int orderId = 1;
        MedicineDTO medicineDTO = new MedicineDTO();
        medicineDTO.setId(26);
        orderItemDTO.setMedicineDTO(medicineDTO);
        orderItemDTO.setAmount(100);

        String json = TestUtil.json(orderItemDTO);
        this.mockMvc.perform(put(URL_PREFIX + "/" + orderId +"/new-order-item").contentType(content).content(json)).
                andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    @Rollback
    public void testGetMedicineInquiriesNotFoundPharmacy() throws Exception
    {
        this.mockMvc.perform(get(URL_PREFIX + "/9999/medicine-inquiries")).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    @Rollback
    public void testGetMedicineInquiriesValid() throws Exception
    {
        this.mockMvc.perform(get(URL_PREFIX + "/1/medicine-inquiries")).andExpect(status().isOk());
    }

}
