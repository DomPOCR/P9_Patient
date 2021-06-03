package com.mediscreen.patient.UT_controller;


import com.mediscreen.patient.controller.PatientController;
import com.mediscreen.patient.model.Patient;
import com.mediscreen.patient.service.PatientService;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(SpringExtension.class)
@WebMvcTest(PatientController.class)
public class PatientControllerTest {

    String firstNameTest = "James";
    String emptyfirstNameTest = null;
    String lastNameTest = "Bond";
    // Constantes pour le jeu de test
    String birthdateTest = "01/01/1963";
    LocalDate birthdateLocal;
    String genreTest = "M";
    String addressTest = "10 downing St";
    String phoneTest = "0123456789";

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private PatientService patientService;

    @BeforeEach
    public void setUpEach() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        birthdateLocal = LocalDate.parse(birthdateTest, df);
    }

    @Test
    public void listPatientTest() throws Exception {

        List<Patient> patientList = new ArrayList<>();
        Patient patientTest = new Patient(firstNameTest, lastNameTest, addressTest, birthdateLocal, phoneTest, genreTest);

        // GIVEN
        patientList.add(patientTest);
        Mockito.when(patientService.findAll()).thenReturn(patientList);

        // WHEN
        // THEN

        mockMvc.perform(get("/patient/list")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(view().name("patient/list"))
                .andExpect(status().isOk());
        Mockito.verify(patientService, Mockito.times(1)).findAll();
    }


    /* Show the list of Patients */

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

    /* Display patient adding form */

    /* Validate correct patient */
    @Test
    public void validatePatientReturnOKTest() throws Exception {

        Patient patientTest = new Patient(firstNameTest, lastNameTest, addressTest, birthdateLocal, phoneTest, genreTest);

        // GIVEN
        Mockito.when(patientService.findById(anyInt())).thenReturn(Optional.of(patientTest));

        // WHEN
        // THEN return the list page
        mockMvc.perform(post("/patient/validate")
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
                .andExpect(view().name("redirect:/patient/list"))
                .andExpect(status().is3xxRedirection());

        Mockito.verify(patientService, Mockito.times(1)).savePatient(any(Patient.class));
    }

    /* Validate incorrect patient */
    @Test
    public void validatePatientReturnNotOKTest() throws Exception {

        // GIVEN : patient not found
        Mockito.when(patientService.findById(anyInt())).thenReturn(null);

        // WHEN
        // THEN stay to validate page

        try {
            this.mockMvc.perform(post("/patient/validate"));
        } catch (Exception e) {
            assertEquals(e.getMessage(), "/patient/validate : KO");
        }
    }

    /* Display patient updating form */
    @Test
    void updatePatient_ShowUpdateForm() throws Exception {

        //GIVEN : Give an exiting patient
        Patient patientTest = new Patient(firstNameTest, lastNameTest, addressTest, birthdateLocal, phoneTest, genreTest);
        Mockito.when(patientService.findById(anyInt())).thenReturn(Optional.of(patientTest));

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
    void updatePatient_CorrectPatient() throws Exception {

        //GIVEN : Give an exiting patient
        Patient patientTest = new Patient(firstNameTest, lastNameTest, addressTest, birthdateLocal, phoneTest, genreTest);
        Mockito.when(patientService.findById(anyInt())).thenReturn(Optional.of(patientTest));

        //WHEN //THEN return the list page after update
        this.mockMvc.perform(post("/patient/update/1")
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
                .andExpect(view().name("redirect:/patient/list"))
                .andExpect(status().is3xxRedirection());

        Mockito.verify(patientService, Mockito.times(1)).savePatient(any(Patient.class));
    }

    /* Update incorrect patient  */
    @Test
    void updatePatient_InCorrectPatient() throws Exception {

        //GIVEN : Patient not found
        Mockito.when(patientService.findById(anyInt())).thenReturn(null);

        // WHEN firstName is empty
        // THEN stay to validate page

        try {
            this.mockMvc.perform(post("/patient/update/1")
                    .param("firstName", emptyfirstNameTest)
                    .param("lastName", lastNameTest)
                    .param("address", addressTest)
                    .param("birthdate", String.valueOf(birthdateLocal))
                    .param("phone", phoneTest)
                    .param("genre", genreTest));
        } catch (Exception e) {
            assertEquals(e.getMessage(), "/patient/update : KO");
        }
    }

    /* Display delete a patient */
    @Test
    void deletePatient_PatientListIsReturn() throws Exception {

        //GIVEN : Give an exiting Person
        Patient patientTest = new Patient(firstNameTest, lastNameTest, addressTest, birthdateLocal, phoneTest, genreTest);

        Mockito.when(patientService.findById(anyInt())).thenReturn(Optional.of(patientTest));

        //WHEN //THEN return the list page
        mockMvc.perform(get("/patient/delete/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(view().name("redirect:/patient/list"))
                .andExpect(status().is3xxRedirection());

        Mockito.verify(patientService, Mockito.times(1)).deletePatient(any(Patient.class));
    }

    @Configuration
    static class ContextConfiguration {
        @Bean
        public PatientController getPatientController() {
            return new PatientController();
        }
    }

}
