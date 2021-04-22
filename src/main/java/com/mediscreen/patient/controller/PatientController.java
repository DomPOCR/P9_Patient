package com.mediscreen.patient.controller;

import com.mediscreen.patient.dao.PatientDao;
import com.mediscreen.patient.model.Patient;
import com.mediscreen.patient.service.PatientService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class PatientController {

    @Autowired
    private PatientService patientService;
    @Autowired
    private PatientDao patientDao;

    /*---------------------------  GET  ------------------------------*/

    // Liste des patients
    /**
     * get patient list
     * @return patients list
     */
    @GetMapping(value = "patient/list")
    @ResponseStatus(HttpStatus.OK)
    public String listPatient(Model model) {
       model.addAttribute("patient",patientDao.findAll());
       return "patient/list";
    }
   /* public List<Patient> listPatients() {
        return patientService.findAll();
    }*/


    /*---------------------------  POST  -----------------------------*/

    // Ajout d'un patient
    /**
     *
     * @param newPatient
     * @return patient created
     * @throws Exception
     */
    @PostMapping(value = "patient/add")
    public ResponseEntity addPatient(@RequestBody Patient newPatient) throws Exception {

        patientService.addPatient(newPatient);
        return new ResponseEntity(newPatient,HttpStatus.CREATED);

    }

    /*---------------------------  PUT  ------------------------------*/

    // Mise à jour Patient

    @PutMapping("patient/update")
    public ResponseEntity updatePatient(@RequestBody Patient patient) throws Exception {

        patientService.updatePatient(patient);
        return new ResponseEntity(patient,HttpStatus.OK);
    }
}
