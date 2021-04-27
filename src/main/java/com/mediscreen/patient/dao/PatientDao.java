package com.mediscreen.patient.dao;

import com.mediscreen.patient.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface PatientDao extends JpaRepository<Patient, Integer> {

   boolean existsPatientByLastNameAndFirstNameAndBirthdate(String lastname, String firstname, LocalDate birthdate);

    Optional<Patient> findById(Integer id);

}
