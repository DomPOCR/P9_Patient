package com.mediscreen.patient.service;

import com.mediscreen.patient.model.Note;
import com.mediscreen.patient.model.Patient;

import java.util.List;
import java.util.Optional;

public interface PatientService {

    /**
     * get a patient by Id
     * @param id
     * @return patient
     */
    Optional<Patient> findById(int id);

    /**
     * get the list of patient
     * @return list of patient
     */
    List<Patient> findAll();

    /**
     *
     * @param familyName
     * @return list of patient by familyName
     */
    List<Patient> findByFamilyName(String familyName);

    /**
     * save patient
     * @param patient
     * @return patient saved
     */
    Patient savePatient(Patient patient);

    /**
     * delete patient
     * @param patient
     * @return patient deleted
     */
    Patient deletePatient(Patient patient);

}
