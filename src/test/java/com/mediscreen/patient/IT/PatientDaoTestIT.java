package com.mediscreen.patient.IT;

import com.mediscreen.patient.dao.PatientDao;
import com.mediscreen.patient.model.Patient;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class PatientDaoTestIT {

    @Autowired
    private PatientDao patientDao;

    @Test
    public void patientListTest() {

        DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate birthdateLocal = LocalDate.parse("31/12/1999", df);

        Patient patient = new Patient("Jack","Bauer","Av Secret 99 NY",birthdateLocal,"99999900","M");

        // Save
        patient = patientDao.save(patient);
        assertNotNull(patient.getId());
        assertEquals(patient.getFirstName(), "Jack", "Jack");

        // Update
        patient.setPhone("000000099");
        patient = patientDao.save(patient);
        assertEquals(patient.getPhone(),"000000099","000000099");

        // Find
        List<Patient> listResult = patientDao.findAll();
        assertTrue(listResult.size() > 0);



    }
}
