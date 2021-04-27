package com.mediscreen.patient.model;

import javax.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "patient")
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "TINYINT")
    private Integer id;

    @Column(name = "firstname", length = 100)
    @NotNull
    private String firstName;

    @Column(name = "lastname", length = 100)
    @NotNull
    private String lastName;

    @Column(name = "address", length = 200)
    private String address;

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate birthdate;

    @Column(name = "phone", length = 12)
    private String phone;

    @Column(name = "genre", length = 1)
    @NotNull
    private String genre;

    public Patient() {

    }

    public Patient(Integer id, String firstName, String lastName, String address, LocalDate birthdate, String phone, String genre) {

        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.birthdate = birthdate;
        this.phone = phone;
        this.genre = genre;
    }

    public Patient(String firstName, String lastName, String address, LocalDate birthdate, String phone, String genre) {

        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.birthdate = birthdate;
        this.phone = phone;
        this.genre = genre;
    }

    @Override
    public String toString() {
        return "Patient{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", address='" + address + '\'' +
                ", birthdate=" + birthdate +
                ", phone='" + phone + '\'' +
                ", genre='" + genre + '\'' +
                '}';
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }
}
