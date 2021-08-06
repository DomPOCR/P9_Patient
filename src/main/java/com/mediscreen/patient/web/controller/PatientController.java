package com.mediscreen.patient.web.controller;

import com.mediscreen.patient.model.Assessment;
import com.mediscreen.patient.model.Note;
import com.mediscreen.patient.model.Patient;
import com.mediscreen.patient.proxies.AssessmentProxy;
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
import java.util.NoSuchElementException;

@Controller
@CrossOrigin(origins = "*")
public class PatientController {

    // Pour le log4j2
    final Logger logger = LogManager.getLogger(this.getClass().getName());

    @Autowired
    private PatientService patientService;
    @Autowired
    private NoteProxy noteProxy;
    @Autowired
    private AssessmentProxy assessmentProxy;

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
        logger.info("**** patient/list : OK");
        return "patient/list";
    }


    // Ajout d'un patient

    /**
     * Add patient
     * @param newPatient : patient to add
     * @return the form to add patient
     */
    @GetMapping(value = "patient/add")
    @ResponseStatus(HttpStatus.OK)
    public String addPatient(Patient newPatient) {

        logger.info("GET /patient/add : Start for " + newPatient.toString());
        return "patient/add";

    }

    /**
     * Endpoint to validate the info of patient
     *
     * @param patient, patient to be added
     * @param result   technical result
     * @param model    public interface model, model can be accessed and attributes can be added
     * @return the form to add patient
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
        try {
            Patient patient = patientService.findById(id);
            model.addAttribute("patient", patient);
            logger.info("GET /patient/update : OK");
        } catch (NoSuchElementException e) {
            throw new IllegalArgumentException("Invalid patient Id:" + id);
        }
        return "patient/update";
    }

    /**
     * Endpoint to validate the patient updating form
     *
     * @param id the patient id
     * @param patient the patient informations
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
        return "patient/list";
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

        try {
            Patient patient = patientService.findById(id);
            patientService.deletePatient(patient);
            model.addAttribute("patientList", patientService.findAll());
            logger.info("/patient/delete ended for : " + patient);

        } catch (NoSuchElementException e) {
            throw new IllegalArgumentException("Invalid patient Id:" + id);
        }
        return "patient/list";
    }

    // *********************************************  PATIENT's NOTES ***********************************/

    // Liste des notes du patient (via micro-service Note)

    /**
     *
     * @param id patient id
     * @param model add
     * @return patient notes list
     */

    @GetMapping("/patient/patHistory/list/{id}")
    @ResponseStatus(HttpStatus.OK)
    public String getPatientNoteByPatientId(@PathVariable("id") Integer id, Model model){

        try {
            Patient patient = patientService.findById(id);
            List<Note> noteList = noteProxy.getPatientNoteByPatientId(id);
            Assessment assessment = assessmentProxy.getAssessmentByPatientId(id);
            model.addAttribute("noteList",noteList);
            model.addAttribute("patient",patient);
            model.addAttribute("assessment",assessment);
            logger.info("GET /patient/patHistory/list/ for id : " + patient.getId() + " OK");
        } catch (NoSuchElementException e) {
            throw new IllegalArgumentException("Invalid patient Id:" + id);
        }
        return "patHistory/list";
    }


    // Update des notes du patient (via micro-service Note)

    /**
     * Endpoint to display note updating form
     * @param id patient id
     * @param model update
     * @return patHistory/update
     */

    @GetMapping("/patient/patHistory/update/{id}")
    @ResponseStatus(HttpStatus.OK)
    public String showUpdateNote(@PathVariable("id") String id, Model model){

        Note noteToUpdated = noteProxy.getNote(id);
        model.addAttribute("note",noteToUpdated);
        model.addAttribute("patient",patientService.findById(noteToUpdated.getPatientId()));
        logger.info("GET /patient/patHistory/update/ for patient id : " + noteToUpdated.getPatientId() + " OK");
        return "patHistory/update";
    }

    /**
     * Endpoint to validate the note updating form
     * @param id patient id
     * @param noteId note id
     * @param note note to update
     * @param result note list
     * @param model update
     * @return the list of patients if OK or stay to update if KO
     */
    @PostMapping("/patient/{id}/patHistory/update/{noteId}")
    @ResponseStatus(HttpStatus.OK)
    public String updateNote(@PathVariable("id") Integer id,@PathVariable("noteId") String noteId, @Valid Note note, BindingResult result, Model model) {

        if (result.hasErrors()) {
            logger.error("POST /patient/patHistory/update : KO " + result.getAllErrors());
            return "patHistory/update";
        }
        note.setPatientId(id);
        noteProxy.updateNote(noteId, note);
        model.addAttribute("noteList",noteProxy.getPatientNoteByPatientId(id));
        model.addAttribute("patient",patientService.findById(id));
        model.addAttribute("assessment",assessmentProxy.getAssessmentByPatientId(id));
        logger.info("POST /patient/patHistory/update/ for patient id : " + id + " OK");
        return "patHistory/list";
    }


    /**
     * Delete a note
     * @param id : the patient id
     * @param noteId : the note to delete id
     * @param model list
     * @return note list if ok
     */
    @GetMapping("/patient/{id}/patHistory/delete/{noteId}")
    @ResponseStatus(HttpStatus.OK)
    public String deleteNote(@PathVariable("id") Integer id,@PathVariable("noteId") String noteId, Model model) {

        noteProxy.deleteNote(noteId);
        model.addAttribute("noteList",noteProxy.getPatientNoteByPatientId(id));
        model.addAttribute("assessment",assessmentProxy.getAssessmentByPatientId(id));
        try {
            Patient patient = patientService.findById(id);
            model.addAttribute("patient",patient);
            logger.info("/patient/patHistory/delete/ for patient id : " + id + " OK");
        } catch (NoSuchElementException e) {
            throw new IllegalArgumentException("Invalid patient Id:" + id);
        }
        return "patHistory/list";
    }

    // Ajout des notes du patient (via micro-service Note)

    /**
     * Endpoint to display note adding form
     * @param id : the patient id
     * @param model add
     * @return patHistory/add
     */

    @GetMapping("/patient/patHistory/add/{id}")
    @ResponseStatus(HttpStatus.OK)
    public String showAddNote(@PathVariable("id") Integer id, Model model){

        model.addAttribute("note", new Note());
        try {
            Patient patient = patientService.findById(id);
            model.addAttribute("patient",patient);
            logger.info("GET /patient/patHistory/add/ for patient id : " + id + " OK");
        } catch (NoSuchElementException e) {
            throw new IllegalArgumentException("Invalid patient Id:" + id);
        }
        return "patHistory/add";
    }

    /**
     * Endpoint to validate the note updating form
     * @param patientId patient id
     * @param note note to update
     * @return the list of patients
     */
    @GetMapping("/patient/{id}/patHistory/validate")
    @ResponseStatus(HttpStatus.CREATED)
    public String addNote(@PathVariable("id") Integer patientId, @Valid Note note,BindingResult result, Model model) {

        if (!result.hasErrors()) {
            note.setId(null);
            note.setPatientId(patientId);
            noteProxy.addNote(note);
            model.addAttribute("noteList", noteProxy.getPatientNoteByPatientId(patientId));
            model.addAttribute("assessment",assessmentProxy.getAssessmentByPatientId(patientId));
            try {
                Patient patient = patientService.findById(patientId);
                model.addAttribute("patient", patient);
                logger.info("GET /patient/patHistory/validate for patient id : " + patientId + " OK");
            } catch (NoSuchElementException e) {
                throw new IllegalArgumentException("Invalid patient Id:" + patientId);
            }
            return "patHistory/list";
        }
        logger.error("POST /patient/update : KO " + result.getAllErrors());
        return "patHistory/add";
    }


}
