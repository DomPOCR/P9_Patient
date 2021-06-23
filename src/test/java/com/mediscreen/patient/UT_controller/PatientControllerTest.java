package com.mediscreen.patient.UT_controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.mediscreen.patient.controller.PatientController;
import com.mediscreen.patient.model.Note;
import com.mediscreen.patient.model.Patient;
import com.mediscreen.patient.proxies.NoteProxy;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;


@ExtendWith(SpringExtension.class)
@WebMvcTest(PatientController.class)
@AutoConfigureMockMvc(addFilters = false)
public class PatientControllerTest {

    // Constantes pour le jeu de test

    String firstNameTest = "James";
    String emptyfirstNameTest = null;
    String lastNameTest = "Bond";
    String birthdateTest = "01/01/1963";
    LocalDate birthdateLocal=LocalDate.of(1963,1,1);
    String genreTest = "M";
    String addressTest = "10 downing St";
    String phoneTest = "0123456789";

    String textNoteTest="Le patient déclare n'avoir aucun problème";
    LocalDate dateNoteTest=LocalDate.of(2021,6,21);



    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private PatientService patientService;
    @MockBean
    private NoteProxy noteProxy;

    @BeforeEach
    public void setUpEach() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        birthdateLocal = LocalDate.parse(birthdateTest, df);
    }

    /* **********************************   PATIENT TESTS ******************************************** */

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
                .andExpect(view().name("patient/list"))
                .andExpect(status().isOk());

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
                .andExpect(view().name("patient/list"))
                .andExpect(status().isOk());

        Mockito.verify(patientService, Mockito.times(1)).deletePatient(any(Patient.class));
    }
    /* **********************************   NOTES TESTS ******************************************** */

    /* Show the list of Notes */

    @Test
    public void listNote() throws Exception {

        List<Note> noteList = new ArrayList<>();
        Patient patientTest = new Patient(1,firstNameTest, lastNameTest, addressTest, birthdateLocal, phoneTest, genreTest);
        Note noteTest = new Note("1","Text note",1,LocalDate.now());

        // GIVEN
        noteList.add(noteTest);
        Mockito.when(patientService.findById(anyInt())).thenReturn(Optional.of(patientTest));
        Mockito.when(noteProxy.getPatientNoteByPatientId(patientTest.getId())).thenReturn(noteList);

        // WHEN
        // THEN

        mockMvc.perform(get("/patient/patHistory/list/1"))
                .andDo(print())
                .andExpect(view().name("patHistory/list"))
                .andExpect(status().isOk());
        }

    /* Display note adding form */
    @Test
    public void AddNote_ShowAddForm() throws Exception {

        Patient patientTest = new Patient(1,firstNameTest, lastNameTest, addressTest, birthdateLocal, phoneTest, genreTest);
        // GIVEN

        // WHEN
        Mockito.when(patientService.findById(anyInt())).thenReturn(Optional.of(patientTest));
        // THEN return the add form

        mockMvc.perform(get("/patient/patHistory/add/1"))
                .andDo(print())
                .andExpect(view().name("patHistory/add"))
                .andExpect(status().isOk());
    }

    /* Validate correct note */
    @Test
    public void ValidateNote_CorrectNote() throws Exception {

        List<Note> noteList = new ArrayList<>();
        Patient patientTest = new Patient(1,firstNameTest, lastNameTest, addressTest, birthdateLocal, phoneTest, genreTest);
        Note noteTest = new Note("1","Text note",1,LocalDate.now());

        // GIVEN
        noteList.add(noteTest);
        Mockito.when((noteProxy.addNote(noteTest))).thenReturn(noteTest);
        Mockito.when(patientService.findById(anyInt())).thenReturn(Optional.of(patientTest));
        Mockito.when(noteProxy.getPatientNoteByPatientId(patientTest.getId())).thenReturn(noteList);

        // WHEN
        // THEN

        mockMvc.perform(get("/patient/1/patHistory/validate")

                .param("id", String.valueOf(1))
                .param("textnote", textNoteTest)
                .param("patientId", String.valueOf(1))
                .param("dateNote", String.valueOf(dateNoteTest)))
                .andDo(print())
                .andExpect(view().name("patHistory/list"))
                .andExpect(status().isCreated());
    }

    /* Show the list of Notes after update */

    @Test
    public void UpdateNote_CorrectNote() throws Exception {

        List<Note> noteList = new ArrayList<>();
        Patient patientTest = new Patient(1,firstNameTest, lastNameTest, addressTest, birthdateLocal, phoneTest, genreTest);
        Note noteTest = new Note("1","Text note",1,LocalDate.now());

        // GIVEN
        noteList.add(noteTest);
        Mockito.when(patientService.findById(anyInt())).thenReturn(Optional.of(patientTest));
        Mockito.when(noteProxy.getPatientNoteByPatientId(patientTest.getId())).thenReturn(noteList);

        // WHEN
        // THEN

        mockMvc.perform(get("/patient/patHistory/list/1")
                .param("id", String.valueOf(1))
                .param("textnote", textNoteTest)
                .param("patientId", String.valueOf(1))
                .param("dateNote", String.valueOf(dateNoteTest)))
                .andDo(print())
                .andExpect(view().name("patHistory/list"))
                .andExpect(status().isOk());
    }

    /* Show the list of Notes after delete */

    @Test
    public void deleteNote_CorrectNote() throws Exception {

        List<Note> noteList = new ArrayList<>();
        Patient patientTest = new Patient(2,firstNameTest, lastNameTest, addressTest, birthdateLocal, phoneTest, genreTest);
        Note noteTest = new Note("1","Text note",1,LocalDate.now());

        // GIVEN
        noteList.add(noteTest);
        Mockito.when(patientService.findById(anyInt())).thenReturn(Optional.of(patientTest));
        Mockito.when(noteProxy.getPatientNoteByPatientId(anyInt())).thenReturn(noteList);
        // WHEN
        // THEN

        mockMvc.perform(get("/patient/2/patHistory/delete/1"))
                .andDo(print())
                .andExpect(view().name("patHistory/list"))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteNote_InCorrectNote() throws Exception {

        Patient patientTest = new Patient(1,firstNameTest, lastNameTest, addressTest, birthdateLocal, phoneTest, genreTest);

        // GIVEN
        Mockito.when(patientService.findById(anyInt())).thenReturn(Optional.of(patientTest));
        Mockito.when(noteProxy.getPatientNoteByPatientId(patientTest.getId())).thenReturn(null);

        // WHEN
        // THEN

        try {
            mockMvc.perform(get("/patient/1/patHistory/delete/1"));
        } catch (Exception e) {
            assertEquals(e.getMessage(),"patHistory/delete note with 1 not found");
        }

    }

    @Configuration
    static class ContextConfiguration {
        @Bean
        public PatientController getPatientController() {
            return new PatientController();
        }
    }

}
