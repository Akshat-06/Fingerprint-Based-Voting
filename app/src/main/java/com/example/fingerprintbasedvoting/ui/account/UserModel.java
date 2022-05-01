package com.example.fingerprintbasedvoting.ui.account;

public class UserModel {

    String adharno, email, fname, password, phoneno;

    public UserModel() {
    }

    public UserModel(String adharno, String email, String fname, String password, String phoneno) {
        this.adharno = adharno;
        this.email = email;
        this.fname = fname;
        this.password = password;
        this.phoneno = phoneno;
    }

    public String getAdharno() {
        return adharno;
    }

    public void setAdharno(String adharno) {
        this.adharno = adharno;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneno() {
        return phoneno;
    }

    public void setPhoneno(String phoneno) {
        this.phoneno = phoneno;
    }
}
