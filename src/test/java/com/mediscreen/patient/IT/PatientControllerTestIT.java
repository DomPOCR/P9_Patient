package com.mediscreen.patient.IT;

import com.mediscreen.patient.dao.PatientDao;
import com.mediscreen.patient.model.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false) /*désactive tous les filtres dans la configuration SpringSecurity */
public class PatientControllerTestIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PatientDao patientDao;

    String firstNameTest = "James";
    String lastNameTest = "Bond";
    String emptyfirstNameTest = null;
    // Constantes pour le jeu de test
    String birthdateTest = "01/01/1963";
    LocalDate birthdateLocal = null;
    String genreTest = "M";
    String addressTest = "10 downing St";
    String phoneTest = "0123456789";

    @BeforeEach
    public void setUpEach() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        birthdateLocal = LocalDate.parse(birthdateTest, df);
    }
    /*------------------------------ Post ------------------------------*/

    /* Add validate patient */
    @Test
    void addPatient_ValidatePatient() throws Exception{

        List<Patient> patientsBeforeAdd;
        patientsBeforeAdd = patientDao.findAll();

        //GIVEN

        // WHEN
        // THEN
        mockMvc.perform(MockMvcRequestBuilders.post("/patient/validate")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .param("id", String.valueOf(1))
                .param("firstName", firstNameTest)
                .param("lastName", lastNameTest)
                .param("address", addressTest)
                .param("birthdate", String.valueOf(birthdateLocal))
                .param("phone", phoneTest)
                .param("genre", genreTest))
                .andDo(print())
                .andExpect(view().name("redirect:/patient/list"));

        List<Patient> patientsAfterAdd;
        patientsAfterAdd = patientDao.findAll();

        assertEquals(patientsAfterAdd.size(),patientsBeforeAdd.size()+1);
    }



    /*------------------------------ Get ------------------------------*/

    @Test
    void deletePatient_ExistingPatient() throws Exception{

        //GIVEN
        Patient patientTest = new Patient(firstNameTest, lastNameTest, addressTest, birthdateLocal, phoneTest, genreTest);
        patientDao.save(patientTest);

        List<Patient> patientsBeforeDelete;
        patientsBeforeDelete = patientDao.findAll();

        // WHEN
        // THEN
        mockMvc.perform(MockMvcRequestBuilders.get("/patient/delete/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/patient/list"));

        List<Patient> patientsAfterDelete;
        patientsAfterDelete = patientDao.findAll();

        assertEquals(patientsAfterDelete.size(),patientsBeforeDelete.size()-1);
    }

    @Test
    void deletePatient_Non_ExistingPatient() throws Exception{

        //GIVEN
        Patient patientTest = new Patient(firstNameTest, lastNameTest, addressTest, birthdateLocal, phoneTest, genreTest);
        patientDao.save(patientTest);

        List<Patient> patientsBeforeDelete;
        patientsBeforeDelete = patientDao.findAll();

        // WHEN
        // THEN
        try {
            mockMvc.perform(MockMvcRequestBuilders.get("/patient/delete/999")
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().is3xxRedirection())
                    .andExpect(view().name("redirect:/patient/list"));
        } catch (Exception e) {
            assertTrue(e.getMessage().contains("failed"));
        }
        List<Patient> patientsAfterDelete;
        patientsAfterDelete = patientDao.findAll();

        assertEquals(patientsAfterDelete.size(),patientsBeforeDelete.size());
    }

}
