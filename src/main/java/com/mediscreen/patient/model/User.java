package com.mediscreen.patient.model;

import com.mediscreen.patient.config.ValidPassword;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "TINYINT")
    private Integer id;

    @Column(name="username",length = 125)
    @NotNull
    private String username;

    @Column(name = "password", length = 125)
    @NotNull
    @ValidPassword
    private String password;

    @Column(name="role",length = 125)
    @NotNull
    private String role;


    public User() {
    }

    public User(@NotBlank(message = "Username is mandatory") String username,
                @NotBlank(message = "Password is mandatory") String password,
                @NotBlank(message = "Role is mandatory") String role)
              {
        this.username = username;
        this.password = password;
        this.role=role;

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
