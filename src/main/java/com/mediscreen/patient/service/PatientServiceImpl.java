package com.mediscreen.patient.service;

import com.mediscreen.patient.dao.PatientDao;
import com.mediscreen.patient.model.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class PatientServiceImpl implements PatientService {

    private final PatientDao patientDao;

    @Autowired
    public PatientServiceImpl(PatientDao patientDao) {
        this.patientDao = patientDao;
    }

    @Override
    public Patient findById(int id) throws NoSuchElementException {
        return patientDao.findById(id).orElseThrow(() -> new NoSuchElementException("The patient id : " + id + " does not exist"));
    }

    @Override
    public List<Patient> findAll() {
        return patientDao.findAll();
    }

    @Override
    public List<Patient> findByFamilyName(String familyName) {
        return patientDao.findByLastName(familyName);
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

