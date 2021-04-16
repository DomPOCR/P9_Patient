package com.mediscreen.patient.controller;

import com.mediscreen.patient.model.Patient;
import com.mediscreen.patient.service.PatientService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.util.List;

@RestController
public class PatientController {

    @Autowired
    private PatientService patientService;

    /*---------------------------  GET Find All Patients -----------------------------*/
    /**
     * get patient list
     * @return patients list
     */
    @GetMapping(value = "patients")
    @ResponseStatus(HttpStatus.OK)
    public List<Patient> listPatients() {
        return patientService.findAll();
    }
}
