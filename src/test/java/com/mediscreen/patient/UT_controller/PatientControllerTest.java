package com.mediscreen.patient.UT_controller;

import com.mediscreen.patient.controller.PatientController;
import com.mediscreen.patient.model.Patient;
import com.mediscreen.patient.service.PatientService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PatientController.class)
public class PatientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PatientService patientService;

    // Constantes pour le jeu de test

    String firstnameTest = "James";
    String lastnameTest = "Bond";
    String birthdateTest ="01/01/1963";
    LocalDate birthdateLocal ;
    String sexTest = "M";
    String addressTest = "10 downing St";
    String phoneTest = "0123456789";

    String existingFirstnameTest = "Domp";
    String existingLastnameTest = "Taylor Waters";

    @BeforeEach
    public void setUpEach() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        birthdateLocal = LocalDate.parse(birthdateTest,df);
    }

    @Test
    public void getAllPatientsControllerTest() throws Exception {

        List<Patient> patientList = new ArrayList<>();
        Patient patientTest = new Patient(99,firstnameTest,lastnameTest,birthdateLocal,sexTest,addressTest,phoneTest);

        // GIVEN
        Mockito.when(patientService.findAll()).thenReturn(patientList);

        // WHEN
        // THEN

        mockMvc.perform(MockMvcRequestBuilders.get("/Patients")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$..firstname").value(firstnameTest))
                .andExpect(jsonPath("$..lastname").value(lastnameTest))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(status().isOk());

    }


}
