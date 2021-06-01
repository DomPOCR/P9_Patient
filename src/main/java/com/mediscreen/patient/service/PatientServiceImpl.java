package com.mediscreen.patient.service;

import com.mediscreen.patient.dao.PatientDao;
import com.mediscreen.patient.model.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PatientServiceImpl implements PatientService {

    @Autowired
    PatientDao patientDao;

    @Override
    public Optional<Patient> findPatientById(int id) {
        return patientDao.findById(id);
    }
}
