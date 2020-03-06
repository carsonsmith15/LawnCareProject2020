package com.example.carsonsmith.cs3270finalproject;

import java.io.Serializable;
import java.util.Date;

public class MowingHistory implements Serializable
{
    private String id;
    private String dateMowed;
    private String AmountCharged;

    MowingHistory() {}

    public MowingHistory(String id, String dateMowed, String amountCharged) {
        this.id = id;
        this.dateMowed = dateMowed;
        this.AmountCharged = amountCharged;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDateMowed() {
        return dateMowed;
    }

    public void setDateMowed(String dateMowed) {
        this.dateMowed = dateMowed;
    }

    public String getAmountCharged() {
        return AmountCharged;
    }

    public void setAmountCharged(String amountCharged) {
        AmountCharged = amountCharged;
    }
}
