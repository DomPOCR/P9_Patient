package com.mediscreen.patient.service;

import com.mediscreen.patient.model.Patient;
import com.mediscreen.patient.dao.PatientDao;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientServiceImpl implements PatientService {

    // Pour le log4j2
    final Logger logger = LogManager.getLogger(this.getClass().getName());

    @Autowired
    PatientDao patientDao;

    @Override
    public List<Patient> findAll() {

        logger.info("listPatients start");
        List<Patient> patientList = patientDao.findAll();
        logger.info("Nb of Patients found :"+patientList.size());
        return patientList;
    }
}
