package com.mediscreen.patient.UT_controller;


import com.fasterxml.jackson.databind.BeanProperty;
import com.mediscreen.patient.controller.PatientController;
import com.mediscreen.patient.dao.PatientDao;
import com.mediscreen.patient.model.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.stubbing.OngoingStubbing;
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

import org.mockito.Mockito;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;

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
    String firstNameTestEmpty = null;

    String lastNameTest = "Bond";
    String birthdateTest = "01/01/1963";
    LocalDate birthdateLocal;
    String genreTest = "M";
    String addressTest = "10 downing St";
    String phoneTest = "0123456789";

    String existingFirstnameTest = "Domp";
    String existingLastnameTest = "Taylor Waters";

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

    @Test
    public void validatePatientReturnOKTest() throws Exception {

       Patient patientTest = new Patient(firstNameTest, lastNameTest, birthdateLocal, genreTest, addressTest, phoneTest);

        // GIVEN
       Mockito.when(patientDao.save(any(Patient.class))).thenReturn(patientTest);
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
    }

    @Test
    public void validatePatientReturnNotOKTest() throws Exception {

        Patient patientTest = new Patient(firstNameTest, lastNameTest, birthdateLocal, genreTest, addressTest, phoneTest);

        // GIVEN
        Mockito.when(patientDao.findById(anyLong())).thenReturn(null);

        // WHEN
        // THEN return the list page

            try {
                this.mockMvc.perform(post("/patient/validate"));
           } catch (Exception e) {
                assertEquals(e.getMessage(),"/patient/validate : KO");
            }
    }
}
