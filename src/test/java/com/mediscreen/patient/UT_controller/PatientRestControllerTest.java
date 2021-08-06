package com.mediscreen.patient.UT_controller;

import com.mediscreen.patient.model.Patient;
import com.mediscreen.patient.service.PatientService;
import com.mediscreen.patient.web.controller.PatientController;
import com.mediscreen.patient.web.controller.PatientRestController;
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

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(PatientRestController.class)
@AutoConfigureMockMvc(addFilters = false)
public class PatientRestControllerTest {


    // Constantes pour le jeu de test

    private Patient patientTest;

    String firstNameTest = "James";
    String emptyfirstNameTest = null;
    String lastNameTest = "Bond";
    String birthdateTest = "01/01/1963";
    LocalDate birthdateLocal = LocalDate.of(1963, 1, 1);
    String genreTest = "M";
    String addressTest = "10 downing St";
    String phoneTest = "0123456789";

    String textNoteTest = "Le patient déclare n'avoir aucun problème";
    LocalDate dateNoteTest = LocalDate.of(2021, 6, 21);


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

    /* **********************************   PATIENT TESTS ******************************************** */
    // TODO test KO
    @Test
    public void getPatientByIdTest() throws Exception {

        // GIVEN
        patientTest = new Patient(1, firstNameTest, lastNameTest, addressTest, birthdateLocal, phoneTest, genreTest);
        Mockito.when(patientService.findById(anyInt())).thenReturn(patientTest);

        // WHEN
        // THEN
        mockMvc.perform(get("/patient/1")
               .contentType(MediaType.APPLICATION_JSON)
               .accept(MediaType.APPLICATION_JSON))
               .andDo(print())
               .andExpect(status().isOk());
    }

    // TODO test KO
    @Test
    public void getPatientByFamilyNameTest() throws Exception {

        // GIVEN
        List<Patient> patientList = new ArrayList<>();
        patientTest = new Patient(1, firstNameTest, lastNameTest, addressTest, birthdateLocal, phoneTest, genreTest);
        patientList.add(patientTest);
        Mockito.when(patientService.findByFamilyName(anyString())).thenReturn(patientList);

        // WHEN
        // THEN
        mockMvc.perform(get("/patient/familyname/"+lastNameTest))
              /* .param("familyName", lastNameTest))*/
               .andDo(print())
               .andExpect(status().isOk());
    }

    @Configuration
    static class ContextConfiguration {
        @Bean
        public PatientRestController getPatientRestController() {
            return new PatientRestController();
        }
    }
}
