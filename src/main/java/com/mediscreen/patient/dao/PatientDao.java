package com.mediscreen.patient.dao;

import com.mediscreen.patient.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface PatientDao extends JpaRepository<Patient, Integer> {

    Patient findById (long id);
    boolean existsPatientByLastNameAndFirstNameAndBirthdate(String lastname, String firstname, LocalDate birthdate);

}
