package com.example.carsonsmith.cs3270finalproject;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;

public class PaymentDetailsActivity extends AppCompatActivity
{

    Customer customer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if(customer == null)
        {
            Intent intent = getIntent();
            customer = (Customer) intent.getSerializableExtra("Customer");
        }

        this.customer = customer;

        setTitle(R.string.detailed_customer_payments);

        // Construct the data source
        ArrayList<Payment> arrayofPaymentInvoices = customer.getPaymentArrayList();

        if(arrayofPaymentInvoices != null)
        {
            // Create the adapter to convert the array to views
            PaymentInvoiceAdapter adapter = new PaymentInvoiceAdapter(this, arrayofPaymentInvoices);
            // Attach the adapter to a ListView
            ListView listView = (ListView) findViewById(R.id.detailedInvoiceListView);
            listView.setAdapter(adapter);
        }
        else
        {
            setTitle(R.string.no_payments);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
