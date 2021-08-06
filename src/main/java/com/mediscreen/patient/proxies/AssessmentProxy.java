package com.mediscreen.patient.proxies;

import com.mediscreen.patient.model.Assessment;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "assessment", url = "http://assessment:8083")
public interface AssessmentProxy {

    @GetMapping(value = "assessment/{id}")
    Assessment getAssessmentByPatientId(@PathVariable("id") Integer patientId);

}
