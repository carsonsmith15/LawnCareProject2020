package com.example.carsonsmith.cs3270finalproject;

import com.example.carsonsmith.cs3270finalproject.MowingHistory;
import com.example.carsonsmith.cs3270finalproject.Payment;

import java.io.Serializable;
import java.util.ArrayList;

public class Customer implements Serializable
{

    //region Private Members

    private String customerID;
    private String FirstName;
    private String LastName;
    private String WeeklyPrice;
    private String PhoneNumber;
    private String City;
    private String State;
    private String StreetAddress;
    private String Zip;
    private String FullName;
    private String EmailAddress;

    //endregion

    //region Constructors

    Customer() {}

    public Customer(String firstName, String lastName, String weeklyPrice, String phoneNumber, String city, String state,
                    String streetAddress, String zip, String emailAddress) {
        FirstName = firstName;
        LastName = lastName;
        WeeklyPrice = weeklyPrice;
        PhoneNumber = phoneNumber;
        City = city;
        State = state;
        StreetAddress = streetAddress;
        Zip = zip;
        EmailAddress = emailAddress;
    }

    //endregion

    //region Getters and Setters

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public String getFirstName() {
        return FirstName;
    }

    public String getFullName() {return FirstName + " " + LastName;}

    public String setFullName(String firstName, String lastName) {return firstName + " " + lastName; }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getWeeklyPrice() {
        return WeeklyPrice;
    }

    public void setWeeklyPrice(String weeklyPrice) {
        WeeklyPrice = weeklyPrice;
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

    public String getEmailAddress() {
        return EmailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.EmailAddress = emailAddress;
    }

    public ArrayList<Payment> paymentArrayList;
    private ArrayList<MowingHistory> mowingHistoryArrayList;

    public ArrayList<Payment> getPaymentArrayList() {
        return paymentArrayList;
    }

    public void setPaymentArrayList(ArrayList<Payment> paymentArrayList) {
        this.paymentArrayList = paymentArrayList;
    }

    public ArrayList<MowingHistory> getMowingHistoryArrayList() {
        return mowingHistoryArrayList;
    }

    public void setMowingHistoryArrayList(ArrayList<MowingHistory> mowingHistoryArrayList) {
        this.mowingHistoryArrayList = mowingHistoryArrayList;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    //endregion

}
