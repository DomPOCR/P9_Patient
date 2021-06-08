package com.mediscreen.patient.service;

import com.mediscreen.patient.dao.PatientDao;
import com.mediscreen.patient.model.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PatientServiceImpl implements PatientService {

    private PatientDao patientDao;

    @Autowired
    public PatientServiceImpl(PatientDao patientDao) {
        this.patientDao = patientDao;
    }
    @Override
    public Optional<Patient> findById(int id) {
        return patientDao.findById(id);
    }

    @Override
    public List<Patient> findAll() {
        return patientDao.findAll();
    }

    @Override
    public Patient savePatient(Patient patient) {
        return patientDao.save(patient);
    }

    @Override
    public Patient deletePatient(Patient patient) {
        patientDao.delete(patient);
        return patient;
    }
}

