package com.mediscreen.patient.UT_controller;


import com.mediscreen.patient.controller.PatientController;
import com.mediscreen.patient.dao.PatientDao;
import com.mediscreen.patient.model.Patient;
import com.mediscreen.patient.service.PatientService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import org.mockito.Mockito;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PatientController.class)
public class PatientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PatientService patientService;

    @MockBean
    private PatientDao patientDao;

    // Constantes pour le jeu de test

    String firstNameTest = "James";
    String lastNameTest = "Bond";
    String birthdateTest = "01/01/1963";
    LocalDate birthdateLocal;
    String sexTest = "M";
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

    @Test
    public void getAllPatientsControllerTest() throws Exception {

        List<Patient> patientList = new ArrayList<>();
        Patient patientTest = new Patient(99, firstNameTest, lastNameTest, birthdateLocal, sexTest, addressTest, phoneTest);

        // GIVEN
        patientList.add(patientTest);
        Mockito.when(patientService.findAll()).thenReturn(patientList);

        // WHEN
        // THEN

        mockMvc.perform(get("/Patients")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$..firstName").value(firstNameTest))
                .andExpect(MockMvcResultMatchers.jsonPath("$..lastName").value(lastNameTest))
                .andExpect(status().isOk());

    }

    @Test // TODO null dans les valeurs de patient
    public void addPatientControllerTest() throws Exception {

        // GIVEN

        //Patient patientTest = new Patient(firstNameTest, lastNameTest, birthdateLocal, sexTest, addressTest, phoneTest);

        Patient patientTest = new Patient();

        patientTest.setId(0);
        patientTest.setFirstName(firstNameTest);
        patientTest.setLastName(lastNameTest);
        patientTest.setBirthdate(birthdateLocal);
        patientTest.setSex(sexTest);
        patientTest.setAddress(addressTest);
        patientTest.setPhone(phoneTest);

        Mockito.when(patientDao.existsPatientByLastNameAndFirstNameAndBirthdate(lastNameTest, firstNameTest, birthdateLocal)).thenReturn(false);

        // WHEN
        // THEN

        mockMvc.perform(post("/addPatient")
                .content(asJsonString(patientTest))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());

    }

    @Test // TODO null dans les valeurs de patient
    public void updatePatientControllerTest() throws Exception {

        // GIVEN

        Patient patientTest = new Patient();
        Patient patientTest2 = new Patient();

        patientTest.setId(0);
        patientTest.setFirstName(firstNameTest);
        patientTest.setLastName(lastNameTest);
        patientTest.setBirthdate(birthdateLocal);
        patientTest.setSex(sexTest);
        patientTest.setAddress(addressTest);
        patientTest.setPhone(phoneTest);

        Mockito.when(patientDao.findById(patientTest.getId())).thenReturn(patientTest2);

        // WHEN
        // THEN

        mockMvc.perform(put("/updatePatient")
                .content(asJsonString(patientTest))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

    }



    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
