package com.mediscreen.patient.UT;

import com.mediscreen.patient.dao.PatientDao;
import com.mediscreen.patient.model.Patient;
import com.mediscreen.patient.service.PatientService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
public class PatientServiceTest {

    @MockBean
    PatientDao patientDao;

    @Autowired
    PatientService patientService;

    DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    LocalDate birthdateLocal = LocalDate.parse("31/12/1999", df);

    Patient patient = new Patient(1,"Jack", "Bauer", "Av Secret 99 NY", birthdateLocal, "99999900", "M");

    // Save
    @Test
    public void savePatientTest() {

        //GIVEN

        //WHEN
        when(patientDao.save(patient)).thenReturn(patient);

        //THEN
        assertThat(patientService.savePatient(patient)).isNotNull();
    }

    // Delete
    @Test
    public void deletePatientTest() {

        //GIVEN
        List<Patient> patientList = new ArrayList<>();
        patientList.add(patient);

        //WHEN
        Mockito.doNothing().when(patientDao).delete(patient);

        //THEN
        assertThat(patientService.deletePatient(patient)).isNotNull();
    }

    // Find
    @Test
     public void findAllPatientTest() {

        //GIVEN
        List<Patient> patientList = new ArrayList<>();
        patientList.add(patient);

        //WHEN
        when(patientDao.findAll()).thenReturn(patientList);

        //THEN
        assertThat(patientService.findAll().size()).isEqualTo(1);
    }

    // Find by id
    @Test
    public void findById_existingPatientIdTest(){
        //GIVEN
        when(patientDao.findById(0)).thenReturn(java.util.Optional.ofNullable(patient));

        //WHEN
        Patient patientResult =  patientService.findById(0);
        //THEN
        assertThat(patientResult).isNotNull();
    }

    @Test
    public void findById_inexistingPatientIdTest(){
        //GIVEN
        when(patientDao.findById(anyInt())).thenReturn(java.util.Optional.ofNullable(null));

        //WHEN THEN
        try {
            Patient patientResult =  patientService.findById(0);
        } catch (NoSuchElementException e) {
            assertThat(e.getMessage().contains("The patient id 0 does not exist"));
        }


    }
    // Find by Family name

    @Test
    public void findByFamilyName_existingFamilyNamePatient_patientListIsReturnTest(){
        //GIVEN
        List<Patient> patientList = new ArrayList<>();
        patientList.add(patient);
        when(patientDao.findByLastName (anyString())).thenReturn(patientList);

        //WHEN
        List<Patient> patientListResult =  patientService.findByFamilyName("Bauer");
        //THEN
        assertThat(patientListResult).isNotNull();
        assertThat(patientListResult.size()).isEqualTo(patientList.size());
    }

    @Test
    public void findByFamilyName_inexistingFamilyNamePatient_patientListIsReturnTest(){
        //GIVEN

        when(patientDao.findByLastName (anyString())).thenReturn(null);

        //WHEN
        List<Patient> patientListResult =  patientService.findByFamilyName("Ghost");
        //THEN
        assertThat(patientListResult).isNull();
    }

}

