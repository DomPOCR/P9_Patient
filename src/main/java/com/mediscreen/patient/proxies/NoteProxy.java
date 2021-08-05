package com.mediscreen.patient.proxies;

import com.mediscreen.patient.config.FeignClientConfiguration;
import com.mediscreen.patient.model.Note;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@FeignClient(name = "note", url = "http://localhost:8082",configuration = FeignClientConfiguration.class)
@FeignClient(name = "note", url = "http://localhost:8082")
public interface NoteProxy {

    @GetMapping(value = "/patHistory/list")
    List<Note> getAllNote();

    @GetMapping(value = "/patHistory/{id}")
    Note getNote(@PathVariable("id") String id);

    @GetMapping(value = "/patHistory/patient/{patientId}")
    List<Note> getPatientNoteByPatientId(@PathVariable("patientId") Integer patientId);

    @PostMapping(value = "/patHistory/add")
    Note addNote(@RequestBody Note note);

    @PutMapping(value = "/patHistory/update/{id}")
    Note updateNote(@PathVariable("id") String id,@RequestBody Note note);

    @PostMapping(value = "/patHistory/delete/{id}")
    Note deleteNote(@PathVariable("id") String id);
}
