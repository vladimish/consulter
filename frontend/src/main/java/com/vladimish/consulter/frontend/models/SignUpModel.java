package com.vladimish.consulter.frontend.models;

import java.util.Objects;

public class SignUpModel {
    public String validate() {
        if (!Objects.equals(this.Password, this.RepeatPassword)) {
            return "PASSWORDS ARE DIFFERENT.";
        }

        return null;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    private String FirstName;

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    private String LastName;

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    private String Email;

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    private String Password;

    public String getRepeatPassword() {
        return RepeatPassword;
    }

    public void setRepeatPassword(String repeatPassword) {
        RepeatPassword = repeatPassword;
    }

    private String RepeatPassword;
}
