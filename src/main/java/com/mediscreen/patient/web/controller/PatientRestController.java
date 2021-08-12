package com.mediscreen.patient.web.controller;

import com.mediscreen.patient.model.Patient;
import com.mediscreen.patient.service.PatientService;
import com.mediscreen.patient.web.exception.NotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@CrossOrigin(origins = "*")
@RestController
public class PatientRestController {

    // Pour le log4j2
    final Logger logger = LogManager.getLogger(this.getClass().getName());

    @Autowired
    private PatientService patientService;

    // Patient par id

    /**
     *
     * @param id Patient Id
     * @return patient
     * @throws NotFoundException if patient not found
     */

    @GetMapping(value = "/patient/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Patient getPatientById(@PathVariable Integer id) throws NotFoundException {

        Patient resultPatient;
        try {
            resultPatient = patientService.findById(id);
        } catch (NoSuchElementException e) {
            logger.warn("The patient id : " + id + " does not exist");
            throw new NotFoundException("The patient whith id : " + id + " does not exist");
        }
        logger.info("getPatientById " + id + " Ok");
        return resultPatient;
    }
    // Liste des patients par nom de famille

    /**
     *
     * @param familyName Name of family
     * @return patient list
     * @throws NotFoundException if family name not exist
     */
    @GetMapping(value = "/patient/familyname/{familyName}")
    @ResponseStatus(HttpStatus.OK)
    public List<Patient> getPatientByFamilyName(@PathVariable String familyName) throws NotFoundException {

        List<Patient> resultPatient = patientService.findByFamilyName(familyName);
        if ((resultPatient == null) || (resultPatient.size() == 0)) {
            logger.warn("The patient whith family name : " + familyName + " does not exist");
            throw new NotFoundException("The patient whith family name : " + familyName + " does not exist");
        }
        logger.info("getPatientByFamilyName " + familyName + " Ok");
        return resultPatient;
    }
}
