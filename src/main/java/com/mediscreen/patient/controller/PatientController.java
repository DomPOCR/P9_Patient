package com.mediscreen.patient.controller;

import com.mediscreen.patient.model.Patient;
import com.mediscreen.patient.service.PatientService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PatientController {

    @Autowired
    private PatientService patientService;

    /*---------------------------  GET  ------------------------------*/

    // Liste des patients
    /**
     * get patient list
     * @return patients list
     */
    @GetMapping(value = "Patients")
    @ResponseStatus(HttpStatus.OK)
    public List<Patient> listPatients() {
        return patientService.findAll();
    }


    /*---------------------------  POST  -----------------------------*/

    // Ajout d'un patient
    /**
     *
     * @param newPatient
     * @return patient created
     * @throws Exception
     */
    @PostMapping(value = "addPatient")
    public ResponseEntity addPatient(@RequestBody Patient newPatient) throws Exception {

        patientService.addPatient(newPatient);
        return new ResponseEntity(newPatient,HttpStatus.CREATED);

    }

    /*---------------------------  PUT  ------------------------------*/

    // Mise à jour Patient

    @PutMapping("updatePatient")
    public ResponseEntity updatePatient(@RequestBody Patient patient) throws Exception {

        patientService.updatePatient(patient);
        return new ResponseEntity(patient,HttpStatus.OK);
    }
}
