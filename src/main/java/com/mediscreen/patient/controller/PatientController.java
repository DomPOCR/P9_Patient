package com.mediscreen.patient.controller;

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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.Valid;

/*@CrossOrigin(origins = "*")*/
@Controller
public class PatientController {

    // Pour le log4j2
    final Logger logger = LogManager.getLogger(this.getClass().getName());

    private PatientService patientService;

    private NoteProxy noteProxy;


    @Autowired
    public PatientController(PatientService patientService, NoteProxy noteProxy) {
        this.patientService = patientService;
        this.noteProxy = noteProxy;
    }

    public PatientController() {

    }

    // *******************************************  PATIENT ***********************************/


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


   // Ajout d'un patient

    /**
     * Add patient
     * @param newPatient
     * @return
     */
    @GetMapping(value = "patient/add")
    @ResponseStatus(HttpStatus.OK)
    public String addPatient(Patient newPatient) {

        logger.info("GET /patient/add : Start");
        return "patient/add";

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
            model.addAttribute("patientList", patientService.findAll());
            logger.info("POST /patient/validate : OK" + patient.toString());
            return "redirect:/patient/list";
        }
        logger.error("/patient/validate : KO" + patient.toString());
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
        model.addAttribute("patientList", patientService.findAll());
        logger.info("/patient/update ended for : " + patient);
        return "/patient/list";
    }

    /**
     * Endpoint to delete a patient
     *
     * @param id    the patient id to delete
     * @param model public interface model, model can be accessed and attributes can be added
     * @return patient/list if ok
     */
    @GetMapping("/patient/delete/{id}")
    @ResponseStatus(HttpStatus.OK)
    public String deletePatient(@PathVariable("id") Integer id, Model model) {
        Patient patient = patientService.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        patientService.deletePatient(patient);
        model.addAttribute("patientList", patientService.findAll());
        logger.info("/patient/delete ended for : " + patient);
        return "/patient/list";
    }

    // *********************************************  PATIENT's NOTES ***********************************/

    // Liste des notes du patient (via micro-service Note)

    @GetMapping("/patient/patHistory/list/{id}")
    @ResponseStatus(HttpStatus.OK)
    public String getPatientNoteByPatientId(@PathVariable("id") Integer id, Model model){

        Patient patient = patientService.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        model.addAttribute("noteList",noteProxy.getPatientNoteByPatientId(id));
        model.addAttribute("patient",patient);
        logger.info("GET /patient/patHistory/list/ for id : " + patient.getId() + " OK");
        return "/patHistory/list";
    }


    // Update des notes du patient (via micro-service Note)

    /**
     * Endpoint to display note updating form
     * @param id
     * @param model
     * @return patHistory/update
     */

    @GetMapping("/patient/patHistory/update/{id}")
    @ResponseStatus(HttpStatus.OK)
    public String ShowUpdateNote(@PathVariable("id") String id, Model model){

        Note noteToUpdated = noteProxy.getNote(id);
        model.addAttribute("note",noteToUpdated);
        model.addAttribute("patient",patientService.findById(noteToUpdated.getPatientId()));
        logger.info("GET /patient/patHistory/update/ for patient id : " + noteToUpdated.getPatientId() + " OK");
        return "/patHistory/update";
    }

    // Update des notes du patient (via micro-service Note)

    /**
     * Endpoint to validate the note updating form
     * @param id
     * @param noteId
     * @param note
     * @param result
     * @param model
     * @return the list of patients if OK or stay to update if KO
     */
    @PostMapping("/patient/{id}/patHistory/update/{noteId}")
    @ResponseStatus(HttpStatus.OK)
    public String updateNote(@PathVariable("id") Integer id,@PathVariable("noteId") String noteId, @Valid Note note, BindingResult result, Model model) {

        if (result.hasErrors()) {
            logger.error("POST /patient/patHistory/update : KO " + result.getAllErrors());
            return "/patHistory/update";
        }
        note.setPatientId(id);
        Note noteUpdated = noteProxy.updateNote(noteId, note);
        model.addAttribute("noteList",noteProxy.getPatientNoteByPatientId(id));
        model.addAttribute("patient",patientService.findById(id).get());
        logger.info("POST /patient/patHistory/update/ for patient id : " + noteUpdated.getPatientId() + " OK");
        return "/patHistory/list";
    }

    @GetMapping("/patient/{id}/patHistory/delete/{noteId}")
    @ResponseStatus(HttpStatus.OK)
    public String deleteNote(@PathVariable("id") Integer id,@PathVariable("noteId") String noteId, Model model) {

        Note noteDeleted = noteProxy.deleteNote(noteId);
        model.addAttribute("noteList",noteProxy.getPatientNoteByPatientId(id));
        model.addAttribute("patient",patientService.findById(id).get());
        logger.info("GET /patient/patHistory/delete/ for patient id : " + noteDeleted.getPatientId() + " OK");
        return "/patHistory/list";

    }


}
