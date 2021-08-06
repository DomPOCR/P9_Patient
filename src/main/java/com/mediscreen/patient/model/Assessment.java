package com.mediscreen.patient.model;

public class Assessment {

    private Integer patientId;
    private String patientFirstName;
    private String patientLastName;
    private int patientAge;
    private String patientSex;
    private Integer assessmentNbOfTrigger;
    private String assessmentLevel;

    public Assessment() {
    }

    public Assessment(Integer patientId, String patientFirstName, String patientLastName, int patientAge, String patientSex, Integer assessmentNbOfTrigger, String assessmentLevel) {
        this.patientId = patientId;
        this.patientFirstName = patientFirstName;
        this.patientLastName = patientLastName;
        this.patientAge = patientAge;
        this.patientSex = patientSex;
        this.assessmentNbOfTrigger = assessmentNbOfTrigger;
        this.assessmentLevel = assessmentLevel;
    }

    public Integer getPatientId() {
        return patientId;
    }

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
    }

    public String getPatientFirstName() {
        return patientFirstName;
    }

    public void setPatientFirstName(String patientFirstName) {
        this.patientFirstName = patientFirstName;
    }

    public String getPatientLastName() {
        return patientLastName;
    }

    public void setPatientLastName(String patientLastName) {
        this.patientLastName = patientLastName;
    }

    public int getPatientAge() {
        return patientAge;
    }

    public void setPatientAge(int patientAge) {
        this.patientAge = patientAge;
    }

    public String getPatientSex() {
        return patientSex;
    }

    public void setPatientSex(String patientSex) {
        this.patientSex = patientSex;
    }

    public Integer getAssessmentNbOfTrigger() {
        return assessmentNbOfTrigger;
    }

    public void setAssessmentNbOfTrigger(Integer assessmentNbOfTrigger) {
        this.assessmentNbOfTrigger = assessmentNbOfTrigger;
    }

    public String getAssessmentLevel() {
        return assessmentLevel;
    }

    public void setAssessmentLevel(String assessmentLevel) {
        this.assessmentLevel = assessmentLevel;
    }
}
