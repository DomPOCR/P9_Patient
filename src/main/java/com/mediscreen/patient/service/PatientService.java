package com.mediscreen.patient.service;

import com.mediscreen.patient.model.Patient;

import java.util.List;

public interface PatientService {

    /**
     * get the list of patient
     * @return list of patient
     */
    public List<Patient> findAll();

    boolean addPatient(Patient newPatient) throws Exception;

    boolean updatePatient(Patient patient) throws Exception;
}
