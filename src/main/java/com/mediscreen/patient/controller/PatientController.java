package com.mediscreen.patient.controller;

import com.mediscreen.patient.dao.PatientDao;
import com.mediscreen.patient.model.Patient;
import com.mediscreen.patient.service.PatientService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "*")
@Controller
public class PatientController {

    // Pour le log4j2
    final Logger logger = LogManager.getLogger(this.getClass().getName());

    @Autowired
    private PatientService patientService;

    /*---------------------------  GET  ------------------------------*/

    // Liste des patients

    /**
     * get patient list
     *
     * @return patients list
     */
    @GetMapping(value = "/patient/list")
    @ResponseStatus(HttpStatus.OK)
    public String listPatient(Model model) {
        model.addAttribute("patientList", patientService.findAll());
        logger.info("patient/list : OK");
        return "patient/list";
    }

    /**
     * Endpoint to validate the info of patient
     *
     * @param patient, patient to be added
     * @param result   technical result
     * @param model    public interface model, model can be accessed and attributes can be added
     * @return
     */
    @PostMapping("/patient/validate")
    public String validate(@Valid Patient patient, BindingResult result, Model model) {

        if (!result.hasErrors()) {
            patientService.savePatient(patient);
            model.addAttribute("patient", patientService.findAll());
            logger.info("POST /patient/validate : OK" + patient.toString());
            return "redirect:/patient/list";
        }
        logger.error("/patient/validate : KO" + patient.toString());
        return "patient/add";
    }

    // Ajout d'un patient

    /**
     * @param newPatient
     * @return patient created
     * @throws Exception
     */
    @GetMapping(value = "patient/add")
    @ResponseStatus(HttpStatus.OK)
    public String addPatient(Patient newPatient) {

        logger.info("GET /patient/add : Start");
        return "patient/add";

    }

    // Mise à jour Patient

    /**
     * Endpoint to display patient updating form
     *
     * @param id    the patient id
     * @param model public interface model, model can be accessed and attributes can be added
     * @return patient/update if OK
     */
    @GetMapping("/patient/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        Patient patient = patientService.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        model.addAttribute("patient", patient);
        logger.info("GET /patient/update : OK");
        return "patient/update";
    }

    /**
     * Endpoint to validate the patient updating form
     *
     * @param id
     * @param patient the patient id
     * @param result  technical result
     * @param model   public interface model, model can be accessed and attributes can be added
     * @return patient/list if ok or patient/update if ko
     */
    @PostMapping("/patient/update/{id}")
    public String updatePatient(@PathVariable("id") Integer id, @Valid Patient patient,
                                BindingResult result, Model model) {

        patient.setId(id);

        if (result.hasErrors()) {
            logger.error("POST /patient/update : KO " + result.getAllErrors());
            return "patient/update";
        }

        patientService.savePatient(patient);
        model.addAttribute("patients", patientService.findAll());
        logger.info("POST /patient/update : OK");
        return "redirect:/patient/list";
    }

    /**
     * Endpoint to delete a patient
     *
     * @param id    the patient id to delete
     * @param model public interface model, model can be accessed and attributes can be added
     * @return patient/list if ok
     */
    @GetMapping("/patient/delete/{id}")
    public String deletePatient(@PathVariable("id") Integer id, Model model) {
        Patient patient = patientService.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        patientService.deletePatient(patient);
        model.addAttribute("patients", patientService.findAll());
        logger.info("/patient/delete : OK");
        return "redirect:/patient/list";
    }
}
