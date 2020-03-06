package com.example.carsonsmith.cs3270finalproject;


import java.io.Serializable;

public class Payment implements Serializable
{
    private String ID;
    private String paymentAmount;
    private String paymentDate;
    private String cashOrCheck;

    public String getCashOrCheck() {
        return cashOrCheck;
    }

    public void setCashOrCheck(String cashOrCheck) {
        this.cashOrCheck = cashOrCheck;
    }

    Payment() {}

    public Payment(String ID, String paymentAmount, String paymentDate, String cashOrCheck) {
        this.ID = ID;
        this.cashOrCheck = cashOrCheck;
        this.paymentAmount = paymentAmount;
        this.paymentDate = paymentDate;
    }

    public Payment(String ID, String paymentAmount, String paymentDate) {
        this.ID = ID;
        this.paymentAmount = paymentAmount;
        this.paymentDate = paymentDate;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(String paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }
}
