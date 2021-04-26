package com.mediscreen.patient.UT_controller;


import com.mediscreen.patient.controller.PatientController;
import com.mediscreen.patient.dao.PatientDao;
import com.mediscreen.patient.model.Patient;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(SpringExtension.class)
@WebMvcTest(PatientController.class)
public class PatientControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private PatientDao patientDao;

    @Configuration
    static class ContextConfiguration {
        @Bean
        public PatientController getPatientController() {
            return new PatientController();
        }
    }
    // Constantes pour le jeu de test

    String firstNameTest = "James";
    String emptyfirstNameTest;
    String lastNameTest = "Bond";
    String birthdateTest = "01/01/1963";
    LocalDate birthdateLocal;
    String genreTest = "M";
    String addressTest = "10 downing St";
    String phoneTest = "0123456789";

    @BeforeEach
    public void setUpEach() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        birthdateLocal = LocalDate.parse(birthdateTest, df);
    }


    /* Show the list of Patients */

    @Test
    public void listPatientTest() throws Exception {

        List<Patient> patientList = new ArrayList<>();
        Patient patientTest = new Patient(99, firstNameTest, lastNameTest, birthdateLocal, genreTest, addressTest, phoneTest);

        // GIVEN
        patientList.add(patientTest);
        Mockito.when(patientDao.findAll()).thenReturn(patientList);

        // WHEN
        // THEN

        mockMvc.perform(get("/patient/list")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(view().name("patient/list"))
                .andExpect(status().isOk());
        Mockito.verify(patientDao,Mockito.times(1)).findAll();
    }

    /* Display patient adding form */

    @Test
    public void addPatientTest() throws Exception {

        // GIVEN
        // WHEN
        // THEN return the add page

        mockMvc.perform(get("/patient/add")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(view().name("patient/add"))
                .andExpect(status().isOk());
    }

    /* Validate correct patient */
    @Test
    public void validatePatientReturnOKTest() throws Exception {

       Patient patientTest = new Patient(firstNameTest, lastNameTest, birthdateLocal, genreTest, addressTest, phoneTest);

        // GIVEN
        Mockito.when(patientDao.findById(anyLong())).thenReturn(patientTest);

        // WHEN
        // THEN return the list page
        this.mockMvc.perform(post("/patient/validate")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .param("firstname", String.valueOf(firstNameTest))
                .param("lastname", String.valueOf(lastNameTest))
                .param("address", String.valueOf(addressTest))
                .param("birthDate",String.valueOf(birthdateTest))
                .param("genre", String.valueOf(genreTest)))
                .andDo(print())
                .andExpect(view().name("redirect:/patient/list"))
                .andExpect(status().is3xxRedirection());

        Mockito.verify(patientDao,Mockito.times(1)).save(any(Patient.class));
    }

    /* Validate incorrect patient */
    @Test
    public void validatePatientReturnNotOKTest() throws Exception {

        // GIVEN : patient not found
        Mockito.when(patientDao.findById(anyLong())).thenReturn(null);

        // WHEN
        // THEN stay to validate page

            try {
                this.mockMvc.perform(post("/patient/validate"));
           } catch (Exception e) {
                assertEquals(e.getMessage(),"/patient/validate : KO");
            }
    }

    /* Display patient updating form */
    @Test
    void updatePatient_ShowUpdateForm() throws Exception{

        //GIVEN : Give an exiting patient
        Patient patientTest = new Patient(firstNameTest, lastNameTest, birthdateLocal, genreTest, addressTest, phoneTest);
        Mockito.when(patientDao.findById(anyLong())).thenReturn(patientTest);

        //WHEN //THEN return the update page
        mockMvc.perform(get("/patient/update/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("patient/update"));
    }

    /* Update existing patient  */
    @Test
    void updatePatient_CorrectPatient() throws Exception{

        //GIVEN : Give an exiting patient
        Patient patientTest = new Patient(firstNameTest, lastNameTest, birthdateLocal, genreTest, addressTest, phoneTest);
        Mockito.when(patientDao.findById(anyLong())).thenReturn(patientTest);

        //WHEN //THEN return the list page after update
        mockMvc.perform(post("/patient/update/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .param("firstname", String.valueOf(firstNameTest))
                .param("lastname", String.valueOf(lastNameTest))
                .param("address", String.valueOf(addressTest))
                .param("birthDate",String.valueOf(birthdateTest))
                .param("genre", String.valueOf(genreTest)))
                .andDo(print())
                .andExpect(view().name("redirect:/patient/list"))
                .andExpect(status().is3xxRedirection());

        Mockito.verify(patientDao,Mockito.times(1)).save(any(Patient.class));
    }

    /* Update incorrect patient  */
    @Test               // TODO OK return ??
    void updatePatient_InCorrectPatient() throws Exception{

        //GIVEN : Patient not found
        Mockito.when(patientDao.findById(anyLong())).thenReturn(null);

        // WHEN
        // THEN stay to validate page

        try {
            this.mockMvc.perform(post("/patient/update/1")
                    .param("firstname", emptyfirstNameTest));
        } catch (Exception e) {
            assertEquals(e.getMessage(),"/patient/update : KO");
        }
    }

    /* Display delete a patient */
    @Test
    void deletePatient_PatientListIsReturn() throws Exception{

        //GIVEN : Give an exiting Person
        Patient patientTest = new Patient(firstNameTest, lastNameTest, birthdateLocal, genreTest, addressTest, phoneTest);

        Mockito.when(patientDao.findById(anyLong())).thenReturn(patientTest);

        //WHEN //THEN return the list page
        mockMvc.perform(get("/patient/delete/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(view().name("redirect:/patient/list"))
                .andExpect(status().is3xxRedirection());

        Mockito.verify(patientDao,Mockito.times(1)).delete(any(Patient.class));
    }

}
