package com.project.resumebuilder.models;

import com.sun.xml.bind.v2.TODO;

import javax.persistence.*;

@Entity
@Table(name = "User")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String userName;
    private String password;
//    private boolean active;
    private String email;
    private String otp;

    private String verification_code;
    private boolean enabled;

    public String getVerification_code() {
        return verification_code;
    }

    public void setVerification_code(String verification_code) {
        this.verification_code = verification_code;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }




    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

//    public boolean isActive() {
//        return active;
//    }
//    public void setActive(boolean active) {
//        this.active = active;
//    }
}
