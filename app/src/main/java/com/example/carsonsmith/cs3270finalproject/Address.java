package com.example.carsonsmith.cs3270finalproject;

public class Address
{

    //region Private Members

    private String ID;
    private String City;
    private String State;
    private String StreetAddress;
    private String ZipCode;

    //endregion

    //region Constructors

    Address () {}

    public Address(String city, String state, String streetAddress, String zipCode) {
        City = city;
        State = state;
        StreetAddress = streetAddress;
        ZipCode = zipCode;
    }

    public Address(String ID, String city, String state, String streetAddress, String zipCode) {
        this.ID = ID;
        City = city;
        State = state;
        StreetAddress = streetAddress;
        ZipCode = zipCode;
    }

    //endregion

    //region Setters
    public void setID(String ID) {
        this.ID = ID;
    }

    public void setCity(String city) {
        City = city;
    }

    public void setState(String state) {
        State = state;
    }

    public void setStreetAddress(String streetAddress) {
        StreetAddress = streetAddress;
    }

    public void setZipCode(String zipCode) {
        ZipCode = zipCode;
    }

    //endregion

    //region Getters

    public String getID() {
        return ID;
    }

    public String getCity() {
        return City;
    }

    public String getState() {
        return State;
    }

    public String getStreetAddress() {
        return StreetAddress;
    }

    public String getZipCode() {
        return ZipCode;
    }

    //endregion

}
