package com.mediscreen.patient.controller;

import com.mediscreen.patient.dao.PatientDao;
import com.mediscreen.patient.model.Note;
import com.mediscreen.patient.model.Patient;
import com.mediscreen.patient.proxies.NoteProxy;
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
import java.util.List;

@CrossOrigin(origins = "*")
@Controller
public class PatientController {

    // Pour le log4j2
    final Logger logger = LogManager.getLogger(this.getClass().getName());

    private PatientService patientService;

    @Autowired
    private NoteProxy noteProxy;


    @Autowired
    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    public PatientController() {

    }

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
    @ResponseStatus(HttpStatus.OK)
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
    @ResponseStatus(HttpStatus.OK)
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
    @PostMapping("/patient/delete/{id}")
    @ResponseStatus(HttpStatus.OK)
    public String deletePatient(@PathVariable("id") Integer id, Model model) {
        Patient patient = patientService.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        patientService.deletePatient(patient);
        model.addAttribute("patients", patientService.findAll());
        logger.info("/patient/delete : OK");
        return "redirect:/patient/list";
    }

    // Liste des notes du patient (via micro-service Note)

    @GetMapping(value = "/patient/patHistory/list/{id}")
    @ResponseStatus(HttpStatus.OK)
    public String getPatientNoteByPatientId(@PathVariable("id") Integer id, Model model){

        model.addAttribute("noteList",noteProxy.getPatientNoteByPatientId(id));
        model.addAttribute("patient",patientService.findById(id));
        logger.info("/patient/patHistory/list/ for id : " + id + " OK");
        return "/patHistory/list";
    }


    // Liste des notes du patient (via micro-service Note)

    @GetMapping(value = "/patient/patHistory/update/{id}")
    @ResponseStatus(HttpStatus.OK)
    public String updateNote(@PathVariable("id") String id, Model model){

        Note noteToUpdated = noteProxy.getNote(id);
        model.addAttribute("note",noteToUpdated);
        model.addAttribute("patient",patientService.findById(noteToUpdated.getPatientId()));
        logger.info("/patient/patHistory/update/ for id : " + id + " OK");
        return "/patHistory/update";
    }


}
