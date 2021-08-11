package com.mediscreen.patient.service;

import com.mediscreen.patient.model.Patient;

import java.util.List;
import java.util.NoSuchElementException;

public interface PatientService {

    /**
     * get a patient by Id
     * @param id patient id
     * @return patient
     */
    Patient findById(int id) throws NoSuchElementException;

    /**
     * get the list of patient
     * @return list of patient
     */
    List<Patient> findAll();

    /**
     *
     * @param familyName name of family
     * @return list of patient by familyName
     */
    List<Patient> findByFamilyName(String familyName);

    /**
     * save patient
     * @param patient patient to save
     * @return patient saved
     */
    Patient savePatient(Patient patient);

    /**
     * delete patient
     * @param patient patient to delete
     * @return patient deleted
     */
    Patient deletePatient(Patient patient);

}
