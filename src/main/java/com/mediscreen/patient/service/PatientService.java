package com.mediscreen.patient.service;

import com.mediscreen.patient.model.Patient;

import java.util.Optional;

public interface PatientService {

    public Optional<Patient> findPatientById(int id);

}
