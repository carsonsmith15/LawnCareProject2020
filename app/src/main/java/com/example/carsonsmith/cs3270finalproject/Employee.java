package com.example.carsonsmith.cs3270finalproject;

import java.io.Serializable;

public class Employee implements Serializable
{

    //region Private Members

    private String employeeID;
    private String FirstName;
    private String LastName;
    private String HourlyRate;
    private String PhoneNumber;
    private String City;
    private String State;
    private String StreetAddress;
    private String EmailAddress;
    private String Zip;
    private String FullName;

    //endregion

    //region Constructors

    public Employee() {}

    public Employee(String firstName, String lastName, String hourlyRate, String phoneNumber, String city, String state, String streetAddress,
                    String emailAddress, String zip)
    {
        this.FirstName = firstName;
        this.LastName = lastName;
        this.HourlyRate = hourlyRate;
        this.PhoneNumber = phoneNumber;
        this.City = city;
        this.State = state;
        this.StreetAddress = streetAddress;
        this.EmailAddress = emailAddress;
        this.Zip = zip;
    }

    //endregion

    //region Getters and Setters

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getHourlyRate() {
        return HourlyRate;
    }

    public void setHourlyRate(String hourlyRate) {
        HourlyRate = hourlyRate;
    }

    public String getFullName() {
        return FirstName + " " + LastName;
    }

    public void setFullName(String firstName, String lastName) {
        FullName = firstName + " " + lastName;
    }

    public String getEmailAddress() {
        return EmailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        EmailAddress = emailAddress;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public String getStreetAddress() {
        return StreetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        StreetAddress = streetAddress;
    }

    public String getZip() {
        return Zip;
    }

    public void setZip(String zip) {
        Zip = zip;
    }

    //endregion

}
