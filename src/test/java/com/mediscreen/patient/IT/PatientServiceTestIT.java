package com.mediscreen.patient.IT;

import com.mediscreen.patient.dao.PatientDao;
import com.mediscreen.patient.model.Patient;
import com.mediscreen.patient.service.PatientService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PatientServiceTestIT {

    @MockBean
    PatientDao patientDao;

    @Autowired
    PatientService patientService;

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
    DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    LocalDate birthdateLocal = LocalDate.parse("31/12/1999", df);

    Patient patient = new Patient(1,"Jack", "Bauer", "Av Secret 99 NY", birthdateLocal, "99999900", "M");

    // Save
    @Test
    public void savePatient() {

        //GIVEN

        //WHEN
        when(patientDao.save(patient)).thenReturn(patient);

        //THEN
        assertThat(patientService.savePatient(patient)).isNotNull();
    }

    // Delete
    @Test
    public void deletePatient() {

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
     public void findAllPatient() {

        //GIVEN
        List<Patient> patientList = new ArrayList<>();
        patientList.add(patient);

        //WHEN
        when(patientDao.findAll()).thenReturn(patientList);

        //THEN
        assertThat(patientService.findAll().size()).isEqualTo(1);
    }
}

